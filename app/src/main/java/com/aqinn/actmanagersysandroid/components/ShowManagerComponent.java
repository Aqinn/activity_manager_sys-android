package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.ShowManager;
import com.aqinn.actmanagersysandroid.modules.ShowManagerModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/27 3:37 PM
 */
@Singleton
@Component(modules = ShowManagerModule.class)
public interface ShowManagerComponent {

    ShowManager getShowManager();

}
