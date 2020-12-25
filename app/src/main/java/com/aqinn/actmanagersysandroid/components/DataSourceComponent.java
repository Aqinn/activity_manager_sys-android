package com.aqinn.actmanagersysandroid.components;

import com.aqinn.actmanagersysandroid.ShowManager;
import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.modules.ActManagerModule;
import com.aqinn.actmanagersysandroid.modules.DataSourceModule;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Aqinn
 * @date 2020/12/18 4:02 PM
 */
@Singleton
@Component(modules = {DataSourceModule.class, ActManagerModule.class})
public interface DataSourceComponent {

    @ActCreateDataSource
    DataSource getActCreate();

    @ActPartDataSource
    DataSource getActPart();

    @AttendCreateDataSource
    DataSource getAttendCreate();

    @AttendPartDataSource
    DataSource getAttendPart();

    @UserDescDataSource
    DataSource getUserDesc();

    ShowManager getActManager();

}
