package com.aqinn.actmanagersysandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentContainerView;

import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.fragment.LoginFragment;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;

/**
 * @author Aqinn
 * @date 2020/12/26 12:26 PM
 */
public class LoginActivity extends BaseFragmentActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected QMUIFragmentActivity.RootView onCreateRootView(int fragmentContainerId) {
        return new LoginActivity.CustomRootView(this, fragmentContainerId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void init(){
        if (CommonUtils.getNowUsernameFromSP(this) != null && !CommonUtils.getUsernameFromSP(this).equals(CommonUtils.ERR_USER_ID)) {
            Log.d(TAG, "init: 当前已有登录用户，直接跳转至首页");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        LoginFragment fragment = new LoginFragment();
        startFragment(fragment);
    }

    @Override
    public int getContextViewId() {
        return R.layout.activity_login;
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
