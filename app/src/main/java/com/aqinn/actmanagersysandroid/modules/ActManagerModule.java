package com.aqinn.actmanagersysandroid.modules;

import com.aqinn.actmanagersysandroid.ShowManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/19 9:25 AM
 */
@Module
public class ActManagerModule {

     @Provides
     ShowManager providerActManager(){
          return new ShowManager();
     }

}
