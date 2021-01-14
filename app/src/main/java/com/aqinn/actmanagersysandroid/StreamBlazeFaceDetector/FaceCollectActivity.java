package com.aqinn.actmanagersysandroid.StreamBlazeFaceDetector;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentContainerView;

import com.aqinn.actmanagersysandroid.BlazeFaceDetector;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.activity.BaseFragmentActivity;
import com.aqinn.actmanagersysandroid.activity.MainActivity;
import com.aqinn.actmanagersysandroid.fragment.ErrorFragment;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;
import com.aqinn.facerecognize.MobileFaceNetRecognize;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;

/**
 * @author Aqinn
 * @date 2021/1/4 10:45 PM
 */
public class FaceCollectActivity extends BaseFragmentActivity {

    private static final String TAG = "FaceCollectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askForPermission();
        init();
    }

    private void init() {
        if (CommonUtil.getNowUsernameFromSP(this) == null || CommonUtil.getUsernameFromSP(this).equals(CommonUtil.ERR_USER_ID)) {
            Log.d(TAG, "init: 无当前登录用户");
            ErrorFragment fragment = new ErrorFragment("数据错误", "您还没有登录");
            startFragment(fragment);
            return;
        }
//        MobileFaceNetRecognize faceNetRecognize = new MobileFaceNetRecognize();
//        String sdPath = getCacheDir().getAbsolutePath() + "/facem/";
//        System.loadLibrary("tnn_wrapper");
//        faceNetRecognize.init(sdPath);
//        BlazeFaceDetector mFaceDetector = new BlazeFaceDetector();
//        mFaceDetector.init(sdPath, 122, 122, 0.975f, 0.23f, 1, 0);

        FaceCollectFragment faceCollectFragment = new FaceCollectFragment();
        startFragment(faceCollectFragment);
    }

    @Override
    protected QMUIFragmentActivity.RootView onCreateRootView(int fragmentContainerId) {
        return new FaceCollectActivity.CustomRootView(this, fragmentContainerId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public int getContextViewId() {
        return R.layout.activity_face_collect;
    }

    class CustomRootView extends RootView {

        private FragmentContainerView fragmentContainer;

        public CustomRootView(Context context, int fragmentContainerId) {
            super(context, fragmentContainerId);
            fragmentContainer = new FragmentContainerView(context);
            fragmentContainer.setId(fragmentContainerId);
            addView(fragmentContainer, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

        @Override
        public FragmentContainerView getFragmentContainerView() {
            return fragmentContainer;
        }
    }



}
