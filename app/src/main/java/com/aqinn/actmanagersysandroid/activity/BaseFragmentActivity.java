package com.aqinn.actmanagersysandroid.activity;

import com.aqinn.actmanagersysandroid.utils.RetrofitUtils;
import com.qmuiteam.qmui.arch.QMUIFragmentActivity;

import retrofit2.Retrofit;

/**
 * @author Aqinn
 * @date 2020/12/11 7:38 PM
 */
public abstract class BaseFragmentActivity extends QMUIFragmentActivity {

    protected Retrofit getRetrofit() {
        return RetrofitUtils.getRetrofit();
    }

}
