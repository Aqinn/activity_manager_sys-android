package com.aqinn.actmanagersysandroid.service;

import com.aqinn.actmanagersysandroid.data.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Aqinn
 * @date 2020/12/25 10:32 AM
 */
public interface UserActService {

    @POST("useract/{userId}/{actId}")
    Observable<ApiResult> userJoinAct(@Path("userId") Long userId, @Path("actId") Long actId);

    @DELETE("useract/{userId}/{actId}")
    Observable<ApiResult> userQuitAct(@Path("userId") Long userId, @Path("actId") Long actId);

}
