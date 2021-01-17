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
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.retrofitservice.ShowItemService;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Aqinn
 * @date 2021/1/17 2:54 PM
 */
public class RefreshPartService extends Service {

    private static final String TAG = "RefreshPartService";

    private HandlerThread mCheckThread;
    private static boolean requestCheckedThreadRunning = false;
    private RefreshPartService.RequestCheckedThread requestCheckedThread;

    @Inject
    public ShowItemService showItemService;
    @Inject
    @ActPartDataSource
    public DataSource dsActP;
    @Inject
    @AttendPartDataSource
    public DataSource dsAttP;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.getApplicationComponent().inject(this);
        mCheckThread = new HandlerThread("refreshPartInfo");
        mCheckThread.start();
        requestCheckedThread = new RefreshPartService.RequestCheckedThread();
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
                    Thread.sleep(4000);
                    // 请求后台，获取参与的活动的变化
                    Observable<ApiResult<List<ActIntroItem>>> observableActIntroItem
                            = showItemService.getActIntroItem(CommonUtils.getNowUserIdFromSP(MyApplication.getContext()));
                    Disposable d1 = observableActIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(listApiResult -> {
                                Log.d(TAG, "成功加载活动数据, 活动数据为空");
                                List<ActIntroItem> list = new ArrayList<>();
                                String nowUserAccount = CommonUtils.getNowUsernameFromSP(MyApplication.getContext());
                                for (ActIntroItem aii : listApiResult.data) {
                                    if (!aii.getCreator().equals(nowUserAccount))
                                        list.add(aii);
                                }
                                refreshPartActData(list);
                                Log.d(TAG, "成功加载活动数据");
                            }, throwable -> {
                                Log.d(TAG, "MyApplication.onCreate() 加载活动数据出错: " + throwable.getMessage());
                            });
                    // 请求后台，获取参与的活动签到的变化
                    Observable<ApiResult<List<ParticipateAttendIntroItem>>> observableParticipateAttendIntroItem
                            = showItemService.getParticipateAttendIntroItem(CommonUtils.getNowUserIdFromSP(MyApplication.getContext()));
                    Disposable d2 = observableParticipateAttendIntroItem.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(listApiResult -> {
                                Log.d(TAG, "成功加载我参与的签到数据, 活动数据为空");
                                refreshPartAttendData(listApiResult.data);
                                Log.d(TAG, "成功加载我参与的签到数据");
                            }, throwable -> {
                                throwable.printStackTrace();
                                Log.d(TAG, "MyServiceManager.checkData() 加载我参与的签到数据出错: " + throwable.getMessage());
                                Log.d(TAG, "加载我参与的签到数据出错");
                            });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    requestCheckedThreadRunning = false;
                    break;
                }
            }
        }
    }

    private void refreshPartActData(List<ActIntroItem> list) {
        List<ActIntroItem> currentData = dsActP.getDatas();
        List<ActIntroItem> removeList = new ArrayList<>();
        // 去掉无效的活动以及更新
        for (ActIntroItem currentAii : currentData) {
            boolean isDelete = true;
            for (ActIntroItem newAii : list) {
                if (currentAii.getActId().equals(newAii.getActId())) {
                    currentAii.setName(newAii.getName());
                    currentAii.setTime(newAii.getTime());
                    currentAii.setLocation(newAii.getLocation());
                    currentAii.setIntro(newAii.getIntro());
                    currentAii.update(currentAii.getId());
                    isDelete = false;
                    break;
                }
            }
            if (isDelete) {
                currentAii.delete();
                removeList.add(currentAii);
            }
        }
        currentData.removeAll(removeList);
        // 检查新增的活动
        if (currentData.size() != list.size()) {
            for (ActIntroItem newAii : list) {
                boolean isAdd = true;
                for (ActIntroItem currentAii : currentData) {
                    if (newAii.getActId().equals(currentAii.getActId())) {
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                    newAii.save();
                    currentData.add(newAii);
                }
                if (currentData.size() == list.size())
                    break;
            }
        }
        dsActP.notifyAllObserver();
    }

    private void refreshPartAttendData(List<ParticipateAttendIntroItem> list) {
        List<ParticipateAttendIntroItem> currentData = dsAttP.getDatas();
        Log.d(TAG, "refreshPartAttendData: 执行函数前的currentData: " + currentData);
        List<ParticipateAttendIntroItem> removeList = new ArrayList<>();
        // 去掉无效的签到以及更新
        for (ParticipateAttendIntroItem currentPAii : currentData) {
            boolean isDelete = true;
            for (ParticipateAttendIntroItem newPAii : list) {
                if (currentPAii.getAttendId().equals(newPAii.getAttendId())) {
                    currentPAii.setName(newPAii.getName());
                    currentPAii.setTime(newPAii.getTime());
                    currentPAii.setStatus(newPAii.getStatus());
                    currentPAii.setuStatus(newPAii.getuStatus());
                    currentPAii.setType(newPAii.getType());
                    currentPAii.update(currentPAii.getId());
                    isDelete = false;
                    break;
                }
            }
            if (isDelete) {
                currentPAii.delete();
                removeList.add(currentPAii);
            }
        }
        currentData.removeAll(removeList);
        // 检查新增的签到
        if (currentData.size() != list.size()) {
            for (ParticipateAttendIntroItem newPAii : list) {
                boolean isAdd = true;
                for (ParticipateAttendIntroItem currentPAii : currentData) {
                    if (newPAii.getAttendId().equals(currentPAii.getAttendId())) {
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                    newPAii.save();
                    currentData.add(newPAii);
                }
                if (currentData.size() == list.size())
                    break;
            }
        }
        dsAttP.notifyAllObserver();
        Log.d(TAG, "refreshPartAttendData: 执行函数后的currentData: " + currentData);
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
