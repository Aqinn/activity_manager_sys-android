package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.ActManager;
import com.aqinn.actmanagersysandroid.scopes.ManagerScope;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/18 5:34 PM
 */
@ManagerScope
@Component(dependencies = DataSourceComponent.class)
public interface ActManagerComponent {

    void inject(ActManager actManager);

}
