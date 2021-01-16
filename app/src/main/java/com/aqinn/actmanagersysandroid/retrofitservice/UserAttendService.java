package com.aqinn.actmanagersysandroid.retrofitservice;

import com.aqinn.actmanagersysandroid.data.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Aqinn
 * @date 2020/12/25 10:37 AM
 */
public interface UserAttendService {

    @POST("userattend/{userId}/{attendId}/{attendType}")
    Observable<ApiResult> userAttend(@Path("userId") Long userId, @Path("attendId") Long attendId,
                                     @Path("attendType") Integer attendType);

    @GET("userattend/{attendId}")
    Observable<ApiResult> getUserAttendCount(@Path("attendId") Long attendId);

    @GET("userattend/self/{attendId}/{timestamp}")
    Observable<ApiResult> getSelfUserAttendAfterTime(@Path("attendId") Long attendId, @Path("timestamp") Long timestamp);

    @GET("userattend/video/{attendId}/{timestamp}")
    Observable<ApiResult> getVideoUserAttendAfterTime(@Path("attendId") Long attendId, @Path("timestamp") Long timestamp);

}
