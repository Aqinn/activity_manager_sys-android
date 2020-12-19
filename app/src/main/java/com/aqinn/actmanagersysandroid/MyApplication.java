package com.aqinn.actmanagersysandroid;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.aqinn.actmanagersysandroid.components.DaggerDataSourceComponent;
import com.aqinn.actmanagersysandroid.components.DataSourceComponent;
import com.qmuiteam.qmui.QMUILog;
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;

/**
 * @author Aqinn
 * @date 2020/12/11 7:32 PM
 */
public class MyApplication extends Application {

    private static DataSourceComponent dataSourceComponent;

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
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
    }

    public static DataSourceComponent getDataSourceComponent() {
        return dataSourceComponent;
    }

}
