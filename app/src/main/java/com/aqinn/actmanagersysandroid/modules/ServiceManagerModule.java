package com.aqinn.actmanagersysandroid.modules;

import com.aqinn.actmanagersysandroid.presenter.MyServiceManager;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.scopes.ServiceManagerScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/27 3:35 PM
 */
@Module
public class ServiceManagerModule {

    @ServiceManagerScope
    @Provides
    ServiceManager providerServiceManager() {
        return new MyServiceManager();
    }

}
