package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.MyApplication;
import com.aqinn.actmanagersysandroid.scopes.ApplicationScope;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/25 6:23 PM
 */
@ApplicationScope
@Component(dependencies = {DataSourceComponent.class, RetrofitServiceComponent.class})
public interface ApplicationComponent {

    void inject(MyApplication myApplication);

}
