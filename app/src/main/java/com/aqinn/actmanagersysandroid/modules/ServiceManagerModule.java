package com.aqinn.actmanagersysandroid.modules;

import com.aqinn.actmanagersysandroid.presenter.MyServiceManager;
import com.aqinn.actmanagersysandroid.presenter.ServiceManager;
import com.aqinn.actmanagersysandroid.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/27 3:35 PM
 */
@Module
public class ServiceManagerModule {

    @ApplicationScope
    @Provides
    ServiceManager providerServiceManager() {
        return new MyServiceManager();
    }

}
