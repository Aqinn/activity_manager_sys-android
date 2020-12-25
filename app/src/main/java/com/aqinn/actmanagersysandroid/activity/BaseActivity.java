package com.aqinn.actmanagersysandroid.activity;

import com.aqinn.actmanagersysandroid.utils.RetrofitUtils;
import com.qmuiteam.qmui.arch.QMUIActivity;

import retrofit2.Retrofit;

/**
 * @author Aqinn
 * @date 2020/12/11 7:37 PM
 */
public class BaseActivity extends QMUIActivity {

    protected Retrofit getRetrofit() {
        return RetrofitUtils.getRetrofit();
    }

}
