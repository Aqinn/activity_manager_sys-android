package com.aqinn.actmanagersysandroid.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment {

    private static final String TAG = "LoginFragment";


    @BindView(R.id.iv_head)
    QMUIRadiusImageView2 ivHead;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_rm)
    CheckBox cbRm;
    @BindView(R.id.bt_register)
    TextView btRegister;
    @BindView(R.id.ll_extra)
    LinearLayout llExtra;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    @BindView(R.id.tv_copyright)
    TextView tvCopyright;

    @Inject
    @ActCreateDataSource
    public DataSource dsActC;
    @Inject
    @ActPartDataSource
    public DataSource dsActP;
    @Inject
    @AttendCreateDataSource
    public DataSource dsAttC;
    @Inject
    @AttendPartDataSource
    public DataSource dsAttP;
    @Inject
    @UserDescDataSource
    public DataSource dsUsers;
    @Inject
    public ServiceManager serviceManager;

    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_login, null);
        MyApplication.getApplicationComponent().inject(this);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonUtils.getUsernameFromSP(getContext()) != null) {
            cbRm.setChecked(true);
            etUsername.setText(CommonUtils.getUsernameFromSP(getContext()));
            etPassword.setText(CommonUtils.getPwdFromSP(getContext()));
        }
    }

    @OnClick(R.id.bt_register)
    public void toRegister() {
        startFragment(new RegisterFragment());
    }

    @OnClick(R.id.bt_confirm)
    public void toMainActivity() {
        if (!CommonUtils.verifyAccount(etUsername.getText().toString()) || !CommonUtils.verifyPwd(etPassword.getText().toString())) {
            Toast.makeText(getContext(), "请正确填写用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        // 验证登录信息
        serviceManager.login(getActivity(), etUsername.getText().toString(), etPassword.getText().toString(), cbRm.isChecked());
    }

}