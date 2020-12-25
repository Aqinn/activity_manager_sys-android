package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.activity.MainActivity;
import com.aqinn.actmanagersysandroid.scopes.ActvityScope;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/18 4:15 PM
 */
@ActvityScope
@Component(dependencies = {DataSourceComponent.class, RetrofitServiceComponent.class})
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

}
