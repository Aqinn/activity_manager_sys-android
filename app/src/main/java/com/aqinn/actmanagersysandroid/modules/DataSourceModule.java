package com.aqinn.actmanagersysandroid.modules;

import com.aqinn.actmanagersysandroid.datafortest.DataSource;
import com.aqinn.actmanagersysandroid.datafortest.DataSourceCreateActIntroItemTest;
import com.aqinn.actmanagersysandroid.datafortest.DataSourceCreateAttendIntroItemTest;
import com.aqinn.actmanagersysandroid.datafortest.DataSourceParticipateActIntroItemTest;
import com.aqinn.actmanagersysandroid.datafortest.DataSourceParticipateAttendIntroItemTest;
import com.aqinn.actmanagersysandroid.datafortest.DataSourceUserDescTest;
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
        return new DataSourceCreateActIntroItemTest();
    }

    @Singleton
    @ActPartDataSource
    @Provides
    DataSource provideActPart() {
        return new DataSourceParticipateActIntroItemTest();
    }

    @Singleton
    @AttendCreateDataSource
    @Provides
    DataSource provideAttendCreate() {
        return new DataSourceCreateAttendIntroItemTest();
    }

    @Singleton
    @AttendPartDataSource
    @Provides
    DataSource provideAttendPart() {
        return new DataSourceParticipateAttendIntroItemTest();
    }

    @Singleton
    @UserDescDataSource
    @Provides
    DataSource provideUserDesc() {
        return new DataSourceUserDescTest();
    }

}
