package com.aqinn.actmanagersysandroid;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.aqinn.actmanagersysandroid.components.ApplicationComponent;
import com.aqinn.actmanagersysandroid.components.DaggerApplicationComponent;
import com.aqinn.actmanagersysandroid.modules.DataSourceModule;
import com.aqinn.actmanagersysandroid.modules.RetrofitServiceModule;
import com.aqinn.actmanagersysandroid.modules.ServiceManagerModule;
import com.aqinn.actmanagersysandroid.modules.ShowManagerModule;
import com.qmuiteam.qmui.QMUILog;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

import org.litepal.LitePal;


/**
 * @author Aqinn
 * @date 2020/12/11 7:32 PM
 */
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    private static ApplicationComponent applicationComponent;

//    @Inject
//    public UserService userService;
//    @Inject
//    public ActService actService;
//    @Inject
//    public UserActService userActService;
//    @Inject
//    public AttendService attendService;
//    @Inject
//    public UserAttendService userAttendService;
//    @Inject
//    @ActCreateDataSource
//    public DataSource dsActC;
//    @Inject
//    @ActPartDataSource
//    public DataSource dsActP;
//    @Inject
//    @AttendCreateDataSource
//    public DataSource dsAttC;
//    @Inject
//    @AttendPartDataSource
//    public DataSource dsAttP;
//    @Inject
//    @UserDescDataSource
//    public DataSource dsUsers;
//    @Inject
//    public ShowItemService showItemService;

    @SuppressLint("StaticFieldLeak")
    private static Context context;

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
        applicationComponent = DaggerApplicationComponent.builder()
                .dataSourceModule(new DataSourceModule())
                .retrofitServiceModule(new RetrofitServiceModule())
                .showManagerModule(new ShowManagerModule())
                .serviceManagerModule(new ServiceManagerModule())
                .build();
    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
