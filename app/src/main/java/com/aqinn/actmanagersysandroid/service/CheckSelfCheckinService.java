package com.aqinn.actmanagersysandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.activity.VideoCheckInActivity;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.retrofitservice.UserAttendService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Aqinn
 * @date 2021/1/16 11:38 PM
 */
public class CheckSelfCheckinService  extends Service {

    private static final String TAG = "SelfCheckinService";

    private static boolean requestCheckedThreadRunning = false;
    private CheckSelfCheckinService.RequestCheckedThread requestCheckedThread;
    private Long attendId;
    private Long preCheckinTime = 1L;

    @Inject
    public UserAttendService userAttendService;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getApplicationComponent().inject(this);
        requestCheckedThread = new CheckSelfCheckinService.RequestCheckedThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        attendId = intent.getLongExtra("attendId", -1L);
        if (attendId == -1L) {
            Log.d(TAG, "onStartCommand: 没有传递 attendId，你想让我查谁的自助签到信息？");
            stopSelf();
        }
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
                    // 请求后台，获取新增的自助签到信息
                    Thread.sleep(5555);  // 要先睡眠后请求，不然可能 callback 还没设置好
                    Observable<ApiResult> observable = userAttendService.getSelfUserAttendAfterTime(attendId, preCheckinTime);
                    Disposable d = observable.subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io())
                            .subscribe(apiResult -> {
                                if (apiResult.success) {
                                    Log.d(TAG, "获取新增的自助签到信息成功，正在准备推送至 RecycleView 中显示");
                                    JSONArray ja = JSONArray.parseArray(String.valueOf(apiResult.data));
                                    for (int i = ja.size() - 1; i >= 0; i--) {
                                        Message msg = Message.obtain();
                                        msg.obj = ja.getJSONObject(i).get("msg");
                                        VideoCheckInActivity.mHandler.sendMessage(msg);
                                        preCheckinTime = ja.getJSONObject(i).getLong("attendTime");
                                    }
                                } else {
                                    Log.d(TAG, "获取新增的自助签到信息成功，但后台返回了false");
                                }
                            }, throwable -> {
                                throwable.printStackTrace();
                                Log.d(TAG, "获取新增的自助签到信息的网络请求出错，错误信息如下: " + throwable.getMessage());
                            });
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
        Log.d(TAG, "onDestroy: ");
        try {
            if (requestCheckedThread != null) {
                requestCheckedThreadRunning = false;
                requestCheckedThread.interrupt();
                requestCheckedThread = null;
            }
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
