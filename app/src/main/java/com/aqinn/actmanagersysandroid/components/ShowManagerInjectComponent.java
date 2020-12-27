package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.ShowManager;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/27 9:15 PM
 */
@Component(dependencies = DataSourceComponent.class)
public interface ShowManagerInjectComponent {

    void inject(ShowManager showManager);

}
