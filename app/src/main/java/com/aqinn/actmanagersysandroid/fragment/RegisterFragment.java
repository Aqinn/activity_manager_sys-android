package com.aqinn.actmanagersysandroid.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.R;
import com.aqinn.actmanagersysandroid.entity.User;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.utils.CommonUtils;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class RegisterFragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout topbar;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_repeat)
    EditText etPasswordRepeat;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.rb_man)
    RadioButton rbMan;
    @BindView(R.id.rb_girl)
    RadioButton rbGirl;
    @BindView(R.id.et_sex)
    LinearLayout etSex;
    @BindView(R.id.et_intro)
    EditText etIntro;
    @BindView(R.id.gl_input)
    GridLayout glInput;
    @BindView(R.id.bt_register)
    QMUIRoundButton btRegister;

    @Inject
    public ServiceManager serviceManager;

    private Drawable wrongDrawable;
    private Drawable rightDrawable;


    @Override
    protected View onCreateView() {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_register, null);
        MyApplication.getApplicationComponent().inject(this);
        ButterKnife.bind(this, rootView);
        initTopBar();
        initData();
        return rootView;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initData() {
        wrongDrawable = getResources().getDrawable(R.mipmap.wrong_input_128, MyApplication.getContext().getTheme());
        rightDrawable = getResources().getDrawable(R.mipmap.right_input_128, MyApplication.getContext().getTheme());
    }

    private void initTopBar() {
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
        topbar.setTitle("注册账号");
    }

    @OnClick(R.id.bt_register)
    public void register() {
        if (!CommonUtils.verifyAccount(etAccount.getText().toString())
                || !CommonUtils.verifyPwd(etPassword.getText().toString())
                || !etPasswordRepeat.getText().toString().equals(etPassword.getText().toString())) {
            Toast.makeText(getContext(), "请正确填写用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }
        User temp = new User(etAccount.getText().toString(), etPassword.getText().toString(),
                etName.getText().toString(), etContact.getText().toString(),
                rbMan.isChecked() ? 1 : 0, etIntro.getText().toString());
        serviceManager.register(temp, new ServiceManager.RegisterCallback() {
            @Override
            public void onFinish() {
                popBackStack(LoginFragment.class);
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @OnTextChanged(R.id.et_account)
    public void verifyAccountEditText() {
        if (etAccount.getText().toString().isEmpty()) {
            etAccount.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        if (!CommonUtils.verifyAccount(etAccount.getText().toString())) {
            etAccount.setTextColor(getResources().getColor(R.color.edittext_wrong, MyApplication.getContext().getTheme()));
            etAccount.setCompoundDrawablesWithIntrinsicBounds(null, null, wrongDrawable, null);
        } else {
            etAccount.setTextColor(getResources().getColor(R.color.edittext_right, MyApplication.getContext().getTheme()));
            etAccount.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
        }
    }

    @OnTextChanged(R.id.et_password)
    public void verifyPwdEditText() {
        verifyPwdRepeatEditText();
        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        if (!CommonUtils.verifyPwd(etPassword.getText().toString())) {
            etPassword.setTextColor(getResources().getColor(R.color.edittext_wrong, MyApplication.getContext().getTheme()));
            etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, wrongDrawable, null);
        } else {
            etPassword.setTextColor(getResources().getColor(R.color.edittext_right, MyApplication.getContext().getTheme()));
            etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
        }
    }

    @OnTextChanged(R.id.et_password_repeat)
    public void verifyPwdRepeatEditText() {
        if (etPasswordRepeat.getText().toString().isEmpty() && etPassword.getText().toString().isEmpty()) {
            etPasswordRepeat.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        if (etPasswordRepeat.getText().toString().equals(etPassword.getText().toString()) && CommonUtils.verifyPwd(etPassword.getText().toString())) {
            etPasswordRepeat.setTextColor(getResources().getColor(R.color.edittext_right, MyApplication.getContext().getTheme()));
            etPasswordRepeat.setCompoundDrawablesWithIntrinsicBounds(null, null, rightDrawable, null);
        } else {
            etPasswordRepeat.setTextColor(getResources().getColor(R.color.edittext_wrong, MyApplication.getContext().getTheme()));
            etPasswordRepeat.setCompoundDrawablesWithIntrinsicBounds(null, null, wrongDrawable, null);
        }
    }

}