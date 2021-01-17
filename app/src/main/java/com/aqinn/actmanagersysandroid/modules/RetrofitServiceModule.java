package com.aqinn.actmanagersysandroid.modules;

import com.aqinn.actmanagersysandroid.scopes.ApplicationScope;
import com.aqinn.actmanagersysandroid.retrofitservice.ActService;
import com.aqinn.actmanagersysandroid.retrofitservice.AttendService;
import com.aqinn.actmanagersysandroid.retrofitservice.ShowItemService;
import com.aqinn.actmanagersysandroid.retrofitservice.UserActService;
import com.aqinn.actmanagersysandroid.retrofitservice.UserAttendService;
import com.aqinn.actmanagersysandroid.retrofitservice.UserFeatureService;
import com.aqinn.actmanagersysandroid.retrofitservice.UserService;
import com.aqinn.actmanagersysandroid.utils.RetrofitUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aqinn
 * @date 2020/12/25 3:09 PM
 */
@Module
public class RetrofitServiceModule {

    @ApplicationScope
    @Provides
    UserService providerUserService() {
        return RetrofitUtils.getRetrofit().create(UserService.class);
    }

    @ApplicationScope
    @Provides
    ActService providerActService() {
        return RetrofitUtils.getRetrofit().create(ActService.class);
    }

    @ApplicationScope
    @Provides
    AttendService providerAttendService() {
        return RetrofitUtils.getRetrofit().create(AttendService.class);
    }

    @ApplicationScope
    @Provides
    UserActService providerUserActService() {
        return RetrofitUtils.getRetrofit().create(UserActService.class);
    }

    @ApplicationScope
    @Provides
    UserAttendService providerUserAttendService() {
        return RetrofitUtils.getRetrofit().create(UserAttendService.class);
    }

    @ApplicationScope
    @Provides
    ShowItemService providerShowItemService() {
        return RetrofitUtils.getRetrofit().create(ShowItemService.class);
    }

    @ApplicationScope
    @Provides
    UserFeatureService providerUserFeatureService() {
        return RetrofitUtils.getRetrofit().create(UserFeatureService.class);
    }

}
//    @RetrofitServiceScope
//    @Singleton