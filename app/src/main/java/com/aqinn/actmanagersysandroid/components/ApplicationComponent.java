package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.ShowManager;
import com.aqinn.actmanagersysandroid.fragment.ActCenterFragment;
import com.aqinn.actmanagersysandroid.fragment.ActDetailFragment;
import com.aqinn.actmanagersysandroid.fragment.AttendCenterFragment;
import com.aqinn.actmanagersysandroid.fragment.LoginFragment;
import com.aqinn.actmanagersysandroid.fragment.MainFragment;
import com.aqinn.actmanagersysandroid.fragment.PersonalCenterFragment;
import com.aqinn.actmanagersysandroid.fragment.RegisterFragment;
import com.aqinn.actmanagersysandroid.modules.DataSourceModule;
import com.aqinn.actmanagersysandroid.modules.RetrofitServiceModule;
import com.aqinn.actmanagersysandroid.modules.ServiceManagerModule;
import com.aqinn.actmanagersysandroid.modules.ShowManagerModule;
import com.aqinn.actmanagersysandroid.presenter.MyServiceManager;
import com.aqinn.actmanagersysandroid.scopes.ApplicationScope;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/25 6:23 PM
 */
@ApplicationScope
@Component(modules = {
        DataSourceModule.class,
        RetrofitServiceModule.class,
        ServiceManagerModule.class,
        ShowManagerModule.class
})
public interface ApplicationComponent {

    // Fragment
    void inject(MainFragment mainFragment);
    void inject(ActCenterFragment actCenterFragment);
    void inject(ActDetailFragment actDetailFragment);
    void inject(AttendCenterFragment attendCenterFragment);
    void inject(PersonalCenterFragment personalCenterFragment);
    void inject(LoginFragment loginFragment);
    void inject(RegisterFragment registerFragment);

    // ShowManager
    void inject(ShowManager showManager);

    // MyServiceManager
    void inject(MyServiceManager myServiceManager);

}
