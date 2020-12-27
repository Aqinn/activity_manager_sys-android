package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.modules.ServiceManagerModule;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.scopes.ServiceManagerScope;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/27 3:38 PM
 */
@ServiceManagerScope
@Component(modules = ServiceManagerModule.class)
public interface ServiceManagerComponent {

    ServiceManager getServiceManager();

}
