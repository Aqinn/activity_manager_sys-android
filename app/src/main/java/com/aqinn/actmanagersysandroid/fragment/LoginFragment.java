package com.aqinn.actmanagersysandroid.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.activity.MainActivity;
import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.Refreshable;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;
import com.aqinn.actmanagersysandroid.service.ActService;
import com.aqinn.actmanagersysandroid.service.AttendService;
import com.aqinn.actmanagersysandroid.service.ShowItemService;
import com.aqinn.actmanagersysandroid.service.UserActService;
import com.aqinn.actmanagersysandroid.service.UserAttendService;
import com.aqinn.actmanagersysandroid.service.UserService;
import com.aqinn.actmanagersysandroid.utils.CommonUtil;
import com.aqinn.actmanagersysandroid.utils.LoadDialogUtil;
import com.google.gson.internal.LinkedTreeMap;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginFragment extends BaseFragment {

    private static final String TAG = "LoginFragment";


    @BindView(R.id.iv_head)
    ImageView ivHead;
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
        if (CommonUtil.getUsernameFromSP(getContext()) != null) {
            cbRm.setChecked(true);
            etUsername.setText(CommonUtil.getUsernameFromSP(getContext()));
            etPassword.setText(CommonUtil.getPwdFromSP(getContext()));
        }
    }

    @OnClick(R.id.bt_register)
    public void toRegister() {
        startFragment(new RegisterFragment());
    }

    @OnClick(R.id.bt_confirm)
    public void toMainActivity() {
        if (!verifyAccount(etUsername.getText().toString()) || !verifyPwd(etPassword.getText().toString())) {
            Toast.makeText(getContext(), "请正确填写用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        // 验证登录信息
        serviceManager.login(getActivity(), etUsername.getText().toString(), etPassword.getText().toString(), cbRm.isChecked());
    }

    private boolean verifyAccount(String account) {
        if (account == null || account.isEmpty() || account.contains(" "))
            return false;
        String regExp = "^[^0-9][\\w_]{2,19}$";
        return account.matches(regExp);
    }

    private boolean verifyPwd(String pwd) {
        if (pwd == null || pwd.isEmpty() || pwd.contains(" "))
            return false;
        String regExp = "^[\\w_]{6,20}$";
        return pwd.matches(regExp);
    }

}