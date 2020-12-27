package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.presenter.MyServiceManager;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.scopes.ServiceManagerScope;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/27 9:14 PM
 */
@ServiceManagerScope
@Component(dependencies = {DataSourceComponent.class, RetrofitServiceComponent.class, ShowManagerComponent.class})
public interface ServiceManagerInjectComponent {

    void inject(MyServiceManager serviceManager);

}
