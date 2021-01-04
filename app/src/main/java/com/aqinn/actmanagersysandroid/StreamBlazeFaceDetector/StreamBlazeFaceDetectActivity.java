package com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.activity.BaseFragmentActivity;

public class StreamBlazeFaceDetectActivity extends BaseFragmentActivity {
    private static final String TAG = com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector.StreamBlazeFaceDetectActivity.class.getSimpleName();
    private static final int FACE_PERMISSION_QUEST_CAMERA = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.base_activity_layout);


        if (Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG, "begin askForPermission the sdk version is" + Build.VERSION.SDK_INT);
            askForPermission();
            updateUI();
        } else {
            Log.d(TAG, "no need to askForPermission the sdk version is" + Build.VERSION.SDK_INT);
            updateUI();
        }
    }

    public void updateUI() {
        Fragment fragment = new StreamBlazeFaceDetectFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "Activity onResume");
        super.onResume();

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "Activity onPause");
        super.onPause();

    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Activity onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Activity onDestroy");
        super.onDestroy();
    }
}
