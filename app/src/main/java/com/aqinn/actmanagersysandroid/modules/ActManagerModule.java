package com.aqinn.actmanagersysandroid.modules;

import com.aqinn.actmanagersysandroid.ActManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/19 9:25 AM
 */
@Module
public class ActManagerModule {

     @Provides
     ActManager providerActManager(){
          return new ActManager();
     }

}
