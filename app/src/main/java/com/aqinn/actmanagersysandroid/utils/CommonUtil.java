package com.aqinn.actmanagersysandroid.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 普通工具类
 */
public class CommonUtil {

    public static SimpleDateFormat sdf_long;
    public static SimpleDateFormat sdf_short;
    public final static String APP_KEY = "ActManaSysAndroid";
    private static final int CAMERA_ID_ANY = -1;
    private static final int CAMERA_ID_BACK = 99;
    private static final int CAMERA_ID_FRONT = 98;
    private static Long randomLong = 100L;

    static {
        sdf_long = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf_short = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static String getNowString() {
        return sdf_long.format(new Date());
    }

    public static Long getRandomLong() {
        return ++randomLong;
    }

    public static void setCameraIdToSP(Context context, int tag) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(APP_KEY, tag);
        edit.commit();
    }

    public static int getCameraIdFromSP(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        return preferences.getInt(APP_KEY, CAMERA_ID_ANY);
    }

    @Nullable
    public static AppCompatActivity findActivity(Context context) {
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        } else {
            return null;
        }
    }

    public static void showToast(final Toast toast, final int cnt) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        }, 0, 3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt);
    }

}
