package com.aqinn.actmanagersysandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.retrofitservice.UserAttendService;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Aqinn
 * @date 2021/1/16 2:23 PM
 */
public class CheckinCountRefreshService extends Service {

    private static final String TAG = "CheckinCountRefreshServ";

    private HandlerThread mCheckThread;
    private Handler mHandler;
    private static boolean requestCheckedThreadRunning = false;
    private RequestCheckedThread requestCheckedThread;

    @Inject
    public ServiceManager serviceManager;
    @Inject
    @AttendCreateDataSource
    public DataSource dsAttC;
    @Inject
    public UserAttendService userAttendService;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getApplicationComponent().inject(this);
        mCheckThread = new HandlerThread("refreshcheckedCount");
        mCheckThread.start();
        mHandler = new Handler(mCheckThread.getLooper(), msg -> {
            Long attendId = (Long) msg.obj;
            int shouldAttendCount = msg.arg1;
            int haveAttendCount = msg.arg2;
            serviceManager.refreshCreateAttend(attendId, shouldAttendCount, haveAttendCount, shouldAttendCount - haveAttendCount);
            return true;
        });
        requestCheckedThread = new RequestCheckedThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!requestCheckedThreadRunning) {
            requestCheckedThreadRunning = true;
            requestCheckedThread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private class RequestCheckedThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (requestCheckedThreadRunning) {
                try {
                    // 请求后台，获取进行中的签到的人数变化
                    for (int i = 0; i < dsAttC.getDatas().size(); i++) {
                        if (((CreateAttendIntroItem) dsAttC.getDatas().get(i)).getStatus() == 2) {
                            Long attendId = ((CreateAttendIntroItem) dsAttC.getDatas().get(i)).getAttendId();
                            Observable<ApiResult> observable = userAttendService.getUserAttendCount(attendId);
                            Disposable d = observable.subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<ApiResult>() {
                                        @Override
                                        public void accept(ApiResult apiResult) throws Exception {
                                            if (apiResult.success) {
                                                try {
                                                    JSONObject jo = JSONObject.parseObject(String.valueOf(apiResult.data));
                                                    int haveAttendCount = jo.getInteger("haveAttendCount");
                                                    int shouldAttendCount = jo.getInteger("shouldAttendCount");
                                                    Log.d(TAG, "获取签到人数变化的请求成功，解析返回数据成功: haveAttendCount=" + haveAttendCount + ",shouldAttendCount=" + shouldAttendCount);
                                                    Message msg = Message.obtain();
                                                    msg.arg1 = shouldAttendCount;
                                                    msg.arg2 = haveAttendCount;
                                                    msg.obj = attendId;
                                                    mHandler.sendMessage(msg);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Log.d(TAG, "获取签到人数变化的请求成功，但解析返回数据时失败，错误信息如下: " + e.getMessage());
                                                }
                                            } else {
                                                Log.d(TAG, "获取签到人数变化的请求成功，但返回值为 false，错误信息如下: " + apiResult.errMsg);
                                            }
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            throwable.printStackTrace();
                                            Log.d(TAG, "获取签到人数变化的请求失败，报错如下\n" + throwable.getMessage());
                                        }
                                    });
                        }
                    }
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    requestCheckedThreadRunning = false;
                    break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (requestCheckedThread != null) {
                requestCheckedThreadRunning = false;
                requestCheckedThread.interrupt();
                requestCheckedThread = null;
            }
            if (mCheckThread != null) {
                mCheckThread.quitSafely();
                mCheckThread.join();
                mCheckThread = null;
            }
            mHandler = null;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
