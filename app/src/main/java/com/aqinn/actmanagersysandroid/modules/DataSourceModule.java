package com.aqinn.actmanagersysandroid.modules;

import com.aqinn.actmanagersysandroid.data.DataSource;
import com.aqinn.actmanagersysandroid.data.show.DataSourceCreateActIntroItem;
import com.aqinn.actmanagersysandroid.data.show.DataSourceCreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.data.show.DataSourceParticipateActIntroItem;
import com.aqinn.actmanagersysandroid.data.show.DataSourceParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.data.show.DataSourceUserDesc;
import com.aqinn.actmanagersysandroid.qualifiers.ActCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.ActPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendCreateDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.AttendPartDataSource;
import com.aqinn.actmanagersysandroid.qualifiers.UserDescDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/18 4:05 PM
 */
@Module
public class DataSourceModule {

    @Singleton
    @ActCreateDataSource
    @Provides
    DataSource provideActCreate() {
        return new DataSourceCreateActIntroItem();
    }

    @Singleton
    @ActPartDataSource
    @Provides
    DataSource provideActPart() {
        return new DataSourceParticipateActIntroItem();
    }

    @Singleton
    @AttendCreateDataSource
    @Provides
    DataSource provideAttendCreate() {
        return new DataSourceCreateAttendIntroItem();
    }

    @Singleton
    @AttendPartDataSource
    @Provides
    DataSource provideAttendPart() {
        return new DataSourceParticipateAttendIntroItem();
    }

    @Singleton
    @UserDescDataSource
    @Provides
    DataSource provideUserDesc() {
        return new DataSourceUserDesc();
    }

}
