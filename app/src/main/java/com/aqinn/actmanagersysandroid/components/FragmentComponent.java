package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.fragment.LoginFragment;
import com.aqinn.actmanagersysandroid.fragment.RegisterFragment;
import com.aqinn.actmanagersysandroid.scopes.FragmentScope;
import com.aqinn.actmanagersysandroid.fragment.ActCenterFragment;
import com.aqinn.actmanagersysandroid.fragment.ActDetailFragment;
import com.aqinn.actmanagersysandroid.fragment.AttendCenterFragment;
import com.aqinn.actmanagersysandroid.fragment.MainFragment;
import com.aqinn.actmanagersysandroid.fragment.PersonalCenterFragment;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/18 4:23 PM
 */
@FragmentScope
@Component(dependencies = {DataSourceComponent.class, ServiceManagerComponent.class})
public interface FragmentComponent {

    void inject(MainFragment mainFragment);

    void inject(ActCenterFragment actCenterFragment);

    void inject(ActDetailFragment actDetailFragment);

    void inject(AttendCenterFragment attendCenterFragment);

    void inject(PersonalCenterFragment personalCenterFragment);

    void inject(LoginFragment loginFragment);

    void inject(RegisterFragment registerFragment);

}
