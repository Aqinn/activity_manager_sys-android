package com.aqinn.actmanagersysandroid.modules;

import com.aqinn.actmanagersysandroid.ShowManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/27 3:35 PM
 */
@Module
public class ShowManagerModule {

    @Singleton
    @Provides
    ShowManager providerShowManager(){
        return new ShowManager();
    }

}
