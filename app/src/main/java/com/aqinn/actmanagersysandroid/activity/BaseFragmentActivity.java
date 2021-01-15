package com.aqinn.actmanagersysandroid.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aqinn.actmanagersysandroid.utils.RetrofitUtils;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;

import retrofit2.Retrofit;

/**
 * @author Aqinn
 * @date 2020/12/11 7:38 PM
 */
public abstract class BaseFragmentActivity extends QMUIFragmentActivity {

    private static final String TAG = "BaseFragmentActivity";
    private static final int FACE_PERMISSION_QUEST_CAMERA = 1024;

    protected Retrofit getRetrofit() {
        return RetrofitUtils.getRetrofit();
    }

    public void askForPermission() {
        //检测权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "didnt get permission,ask for it!");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.INTERNET},
                    FACE_PERMISSION_QUEST_CAMERA);
        }
    }

}
