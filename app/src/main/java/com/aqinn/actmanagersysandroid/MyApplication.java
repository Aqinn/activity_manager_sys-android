package com.aqinn.actmanagersysandroid;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.aqinn.actmanagersysandroid.components.DaggerApplicationComponent;
import com.aqinn.actmanagersysandroid.components.DaggerDataSourceComponent;
import com.aqinn.actmanagersysandroid.components.DaggerRetrofitServiceComponent;
import com.aqinn.actmanagersysandroid.components.DataSourceComponent;
import com.aqinn.actmanagersysandroid.components.RetrofitServiceComponent;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;
import com.aqinn.actmanagersysandroid.service.ActService;
import com.aqinn.actmanagersysandroid.service.AttendService;
import com.aqinn.actmanagersysandroid.service.ShowItemService;
import com.aqinn.actmanagersysandroid.service.UserActService;
import com.aqinn.actmanagersysandroid.service.UserAttendService;
import com.aqinn.actmanagersysandroid.service.UserService;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;
import com.qmuiteam.qmui.QMUILog;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

import org.litepal.LitePal;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Aqinn
 * @date 2020/12/11 7:32 PM
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private static DataSourceComponent dataSourceComponent;
    private static RetrofitServiceComponent retrofitServiceComponent;

    @Inject
    public UserService userService;
    @Inject
    public ActService actService;
    @Inject
    public UserActService userActService;
    @Inject
    public AttendService attendService;
    @Inject
    public UserAttendService userAttendService;
    @Inject
    @ActCreateDataSource
    public DataSource dsActC;
    @Inject
    @ActPartDataSource
    public DataSource dsActP;
    @Inject
    @AttendCreateDataSource
    public DataSource dsAttC;
    @Inject
    @AttendPartDataSource
    public DataSource dsAttP;
    @Inject
    @UserDescDataSource
    public DataSource dsUsers;
    @Inject
    public ShowItemService showItemService;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Long nowUserId = CommonUtil.ERR_USER_ID;
    public static String nowUserAccount = "";

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(this);
        QMUILog.setDelegete(new QMUILog.QMUILogDelegate() {
            @Override
            public void e(String tag, String msg, Object... obj) {
                Log.e(tag, msg);
            }

            @Override
            public void w(String tag, String msg, Object... obj) {
                Log.w(tag, msg);
            }

            @Override
            public void i(String tag, String msg, Object... obj) {

            }

            @Override
            public void d(String tag, String msg, Object... obj) {

            }

            @Override
            public void printErrStackTrace(String tag, Throwable tr, String format, Object... obj) {

            }
        });
        QMUISwipeBackActivityManager.init(this);
        dataSourceComponent = DaggerDataSourceComponent.create();
        retrofitServiceComponent = DaggerRetrofitServiceComponent.create();
        DaggerApplicationComponent.builder().dataSourceComponent(dataSourceComponent).retrofitServiceComponent(retrofitServiceComponent).build().inject(this);
//        // 初始化数据
//        Disposable disposable = Observable.create((ObservableOnSubscribe<String>) emitter -> {
//            emitter.onNext("加载用户数据");
//            emitter.onNext("加载活动数据");
//            emitter.onNext("加载我创建的签到数据");
//            emitter.onNext("加载我参与的签到数据");
//            emitter.onComplete();
//        }).subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())
//                .subscribe(s -> {
//                    switch (s) {
//                        case "加载用户数据":
//                            Observable<ApiResult<UserDesc>> observableUserDesc = showItemService.getUserDesc(23L);
//                            observableUserDesc.subscribeOn(Schedulers.io())
//                                    .observeOn(Schedulers.io())
//                                    .subscribe(userDescApiResult -> {
//                                        ((Refreshable) dsUsers).refresh(userDescApiResult.data);
//                                        CommonUtil.setNowUserIdToSP(getContext(), userDescApiResult.data.getId());
//                                        nowUserId = CommonUtil.getNowUserIdFromSP(getContext());
//                                        nowUserAccount = userDescApiResult.data.getAccount();
//                                    }, throwable -> {
//                                        Log.d(TAG, "MyApplication.onCreate() 加载用户数据出错: " + throwable.getMessage());
//                                    });
//                            break;
//                        case "加载活动数据":
//                            Observable<ApiResult<List<ActIntroItem>>> observableActIntroItem
//                                    = showItemService.getActIntroItem(23L);
//                            observableActIntroItem.subscribeOn(Schedulers.io())
//                                    .observeOn(Schedulers.io())
//                                    .subscribe(listApiResult -> {
//                                        ((Refreshable) dsActC).refresh(listApiResult.data);
//                                        ((Refreshable) dsActP).refresh(listApiResult.data);
//                                    }, throwable -> {
//                                        Log.d(TAG, "MyApplication.onCreate() 加载活动数据出错: " + throwable.getMessage());
//                                    });
//                            break;
//                        case "加载我创建的签到数据":
//                            Observable<ApiResult<List<CreateAttendIntroItem>>> observableCreateAttendIntroItem
//                                    = showItemService.getCreateAttendIntroItem(23L);
//                            observableCreateAttendIntroItem.subscribeOn(Schedulers.io())
//                                    .observeOn(Schedulers.io())
//                                    .subscribe(listApiResult -> {
//                                        ((Refreshable) dsAttC).refresh(listApiResult.data);
//                                    }, throwable -> {
//                                        Log.d(TAG, "MyApplication.onCreate() 加载我创建的签到数据出错: " + throwable.getMessage());
//                                    });
//                            break;
//                        case "加载我参与的签到数据":
//                            Observable<ApiResult<List<ParticipateAttendIntroItem>>> observableParticipateAttendIntroItem
//                                    = showItemService.getParticipateAttendIntroItem(23L);
//                            observableParticipateAttendIntroItem.subscribeOn(Schedulers.io())
//                                    .observeOn(Schedulers.io())
//                                    .subscribe(listApiResult -> {
//                                        ((Refreshable) dsAttP).refresh(listApiResult.data);
//                                    }, throwable -> {
//                                        Log.d(TAG, "MyApplication.onCreate() 加载我参与的签到数据出错: " + throwable.getMessage());
//                                    });
//                            break;
//                        default:
//                            break;
//                    }
//                }, throwable -> {
//                    Log.d(TAG, "onError: " + throwable.getMessage());
//                }, () -> {
//                    Log.d(TAG, "onComplete: 后台刷新数据完成");
//                });
    }

    public static DataSourceComponent getDataSourceComponent() {
        return dataSourceComponent;
    }

    public static RetrofitServiceComponent getRetrofitServiceComponent() {
        return retrofitServiceComponent;
    }


}
