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
import com.aqinn.actmanagersysandroid.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/18 4:05 PM
 */
@Module
public class DataSourceModule {

    @ApplicationScope
    @ActCreateDataSource
    @Provides
    DataSource provideActCreate() {
        return new DataSourceCreateActIntroItem();
    }

    @ApplicationScope
    @ActPartDataSource
    @Provides
    DataSource provideActPart() {
        return new DataSourceParticipateActIntroItem();
    }

    @ApplicationScope
    @AttendCreateDataSource
    @Provides
    DataSource provideAttendCreate() {
        return new DataSourceCreateAttendIntroItem();
    }

    @ApplicationScope
    @AttendPartDataSource
    @Provides
    DataSource provideAttendPart() {
        return new DataSourceParticipateAttendIntroItem();
    }

    @ApplicationScope
    @UserDescDataSource
    @Provides
    DataSource provideUserDesc() {
        return new DataSourceUserDesc();
    }

}
