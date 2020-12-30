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
    private static final String NOW_USER_KEY = "AMSysNowUserKey";
    private static final String NOW_USER_USERNAME = "AMSysNowUsername";
    private static final String REMEMBER_USERNAME = "RememberUsername";
    private static final String REMEMBER_PWD = "RememberPwd";
    public static final Long ERR_USER_ID = -1L;
    private static final String accountRegExp = "^[a-zA-Z_][0-9a-zA-Z_]{2,19}$";
    private static final String pwdRegExp = "^[0-9a-zA-Z_]{6,20}$";

    static {
        sdf_long = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf_short = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static boolean verifyAccount(String account) {
        if (account == null || account.isEmpty() || account.contains(" "))
            return false;
        return account.matches(accountRegExp);
    }

    public static boolean verifyPwd(String pwd) {
        if (pwd == null || pwd.isEmpty() || pwd.contains(" "))
            return false;
        return pwd.matches(pwdRegExp);
    }

    /**
     * 根据十进制数转换成二进制数，对应哪一位置上有 1 就是哪种签到方式被开启了
     *
     * @param dec
     * @return
     */
    public static Integer[] dec2typeArr(Integer dec) {
        String binStr = Integer.toString(dec.intValue(), 2);
        System.out.println(binStr);
        Integer[] res = new Integer[binStr.replace("0", "").length()];
        int flag = 0;
        for (int i = binStr.length() - 1; i >= 0; i--) {
            if (binStr.charAt(i) == '1')
                res[flag++] = binStr.length() - i;
        }
        return res;
    }

    /**
     * 上面方法 Integer[] dec2typeArr(Integer dec) 的反向过程
     *
     * @return
     */
    public static Integer typeArr2dec(Integer[] typeArr) {
        char[] temp = new char[]{'0', '0', '0', '0', '0', '0'};
        for (Integer integer : typeArr) {
            temp[temp.length - integer] = '1';
        }
        return Integer.parseInt(String.valueOf(temp), 2);
    }

    public static String getNowString() {
        return sdf_long.format(new Date());
    }

    public static Long getRandomLong() {
        return ++randomLong;
    }

    public static void setNowUserIdToSP(Context context, Long tag) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putLong(NOW_USER_KEY, tag);
        edit.commit();
    }

    public static Long getNowUserIdFromSP(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        return preferences.getLong(NOW_USER_KEY, ERR_USER_ID);
    }

    public static void setNowUsernameToSP(Context context, String tag) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(NOW_USER_USERNAME, tag);
        edit.commit();
    }

    public static String getNowUsernameFromSP(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        return preferences.getString(NOW_USER_USERNAME, null);
    }

    public static void setUsernameToSP(Context context, String tag) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(REMEMBER_USERNAME, tag);
        edit.commit();
    }

    public static String getUsernameFromSP(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        return preferences.getString(REMEMBER_USERNAME, null);
    }

    public static void setPwdToSP(Context context, String tag) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(REMEMBER_PWD, tag);
        edit.commit();
    }

    public static String getPwdFromSP(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("ActManaSysAndroidCache", Context.MODE_PRIVATE);
        return preferences.getString(REMEMBER_PWD, null);
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
