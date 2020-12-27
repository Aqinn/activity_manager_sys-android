package com.aqinn.actmanagersysandroid;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.aqinn.actmanagersysandroid.components.DaggerDataSourceComponent;
import com.aqinn.actmanagersysandroid.components.DaggerFragmentComponent;
import com.aqinn.actmanagersysandroid.components.DaggerRetrofitServiceComponent;
import com.aqinn.actmanagersysandroid.components.DaggerServiceManagerComponent;
import com.aqinn.actmanagersysandroid.components.DaggerServiceManagerInjectComponent;
import com.aqinn.actmanagersysandroid.components.DaggerShowManagerComponent;
import com.aqinn.actmanagersysandroid.components.DaggerShowManagerInjectComponent;
import com.aqinn.actmanagersysandroid.components.DataSourceComponent;
import com.aqinn.actmanagersysandroid.components.FragmentComponent;
import com.aqinn.actmanagersysandroid.components.RetrofitServiceComponent;
import com.aqinn.actmanagersysandroid.components.ServiceManagerComponent;
import com.aqinn.actmanagersysandroid.components.ServiceManagerInjectComponent;
import com.aqinn.actmanagersysandroid.components.ShowManagerComponent;
import com.aqinn.actmanagersysandroid.components.ShowManagerInjectComponent;
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

    private static DataSourceComponent dataSourceComponent;
    private static RetrofitServiceComponent retrofitServiceComponent;
    private static ShowManagerComponent showManagerComponent;
    private static ServiceManagerComponent serviceManagerComponent;
    private static FragmentComponent fragmentComponent;
    private static ShowManagerInjectComponent showManagerInjectComponent;
    private static ServiceManagerInjectComponent serviceManagerInjectComponent;

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
        dataSourceComponent = DaggerDataSourceComponent.builder().dataSourceModule(new DataSourceModule()).build();
        retrofitServiceComponent = DaggerRetrofitServiceComponent.builder().retrofitServiceModule(new RetrofitServiceModule()).build();
        showManagerComponent = DaggerShowManagerComponent.builder().showManagerModule(new ShowManagerModule()).build();
        serviceManagerComponent = DaggerServiceManagerComponent.builder().serviceManagerModule(new ServiceManagerModule()).build();
        showManagerInjectComponent = DaggerShowManagerInjectComponent.builder().dataSourceComponent(dataSourceComponent).build();
        serviceManagerInjectComponent = DaggerServiceManagerInjectComponent.builder()
                .dataSourceComponent(dataSourceComponent)
                .retrofitServiceComponent(retrofitServiceComponent)
                .showManagerComponent(showManagerComponent)
                .build();
        fragmentComponent = DaggerFragmentComponent.builder()
                .dataSourceComponent(dataSourceComponent)
                .serviceManagerComponent(serviceManagerComponent)
                .build();
    }

    public static DataSourceComponent getDataSourceComponent() {
        return dataSourceComponent;
    }

    public static RetrofitServiceComponent getRetrofitServiceComponent() {
        return retrofitServiceComponent;
    }

    public static ShowManagerComponent getShowManagerComponent() {
        return showManagerComponent;
    }

    public static ServiceManagerComponent getServiceManagerComponent() {
        return serviceManagerComponent;
    }

    public static FragmentComponent getFragmentComponent() {
        return fragmentComponent;
    }

    public static ShowManagerInjectComponent getShowManagerInjectComponent() {
        return showManagerInjectComponent;
    }

    public static ServiceManagerInjectComponent getServiceManagerInjectComponent() {
        return serviceManagerInjectComponent;
    }
}
