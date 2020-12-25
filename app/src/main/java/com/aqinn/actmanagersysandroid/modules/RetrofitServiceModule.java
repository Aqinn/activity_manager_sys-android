package com.aqinn.actmanagersysandroid.modules;

import com.aqinn.actmanagersysandroid.scopes.RetrofitServiceScope;
import com.aqinn.actmanagersysandroid.service.ActService;
import com.aqinn.actmanagersysandroid.service.AttendService;
import com.aqinn.actmanagersysandroid.service.ShowItemService;
import com.aqinn.actmanagersysandroid.service.UserActService;
import com.aqinn.actmanagersysandroid.service.UserAttendService;
import com.aqinn.actmanagersysandroid.service.UserService;
import com.aqinn.actmanagersysandroid.utils.RetrofitUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/25 3:09 PM
 */
@Module
public class RetrofitServiceModule {

//    @RetrofitServiceScope
    @Provides
    UserService providerUserService() {
        return RetrofitUtils.getRetrofit().create(UserService.class);
    }

//    @RetrofitServiceScope
    @Provides
    ActService providerActService() {
        return RetrofitUtils.getRetrofit().create(ActService.class);
    }

//    @RetrofitServiceScope
    @Provides
    AttendService providerAttendService() {
        return RetrofitUtils.getRetrofit().create(AttendService.class);
    }

//    @RetrofitServiceScope
    @Provides
    UserActService providerUserActService() {
        return RetrofitUtils.getRetrofit().create(UserActService.class);
    }

//    @RetrofitServiceScope
    @Provides
    UserAttendService providerUserAttendService() {
        return RetrofitUtils.getRetrofit().create(UserAttendService.class);
    }

    @Provides
    ShowItemService providerShowItemService() {
        return RetrofitUtils.getRetrofit().create(ShowItemService.class);
    }

}
