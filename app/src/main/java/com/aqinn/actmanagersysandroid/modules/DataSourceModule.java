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
import com.aqinn.actmanagersysandroid.scopes.DataSourceScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/18 4:05 PM
 */
@Module
public class DataSourceModule {

    @ActCreateDataSource
    @Provides
    DataSource provideActCreate() {
        return new DataSourceCreateActIntroItem();
    }

    @ActPartDataSource
    @Provides
    DataSource provideActPart() {
        return new DataSourceParticipateActIntroItem();
    }

    @AttendCreateDataSource
    @Provides
    DataSource provideAttendCreate() {
        return new DataSourceCreateAttendIntroItem();
    }

    @AttendPartDataSource
    @Provides
    DataSource provideAttendPart() {
        return new DataSourceParticipateAttendIntroItem();
    }

    @UserDescDataSource
    @Provides
    DataSource provideUserDesc() {
        return new DataSourceUserDesc();
    }

}
