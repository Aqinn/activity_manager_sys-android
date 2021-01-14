package com.aqinn.actmanagersysandroid.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentContainerView;

import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.fragment.ErrorFragment;
import com.aqinn.actmanagersysandroid.fragment.MainFragment;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;

public class MainActivity extends BaseFragmentActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askForPermission();
        init();
    }

    @Override
    protected QMUIFragmentActivity.RootView onCreateRootView(int fragmentContainerId) {
        return new CustomRootView(this, fragmentContainerId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void init(){
        if (CommonUtils.getNowUsernameFromSP(this) == null || CommonUtils.getUsernameFromSP(this).equals(CommonUtils.ERR_USER_ID)) {
            Log.d(TAG, "init: 无当前登录用户");
            ErrorFragment fragment = new ErrorFragment("数据错误", "您还没有登录");
            startFragment(fragment);
            return;
        }
        MainFragment fragment = new MainFragment();
        startFragment(fragment);
    }

    @Override
    public int getContextViewId() {
        return R.layout.activity_main;
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