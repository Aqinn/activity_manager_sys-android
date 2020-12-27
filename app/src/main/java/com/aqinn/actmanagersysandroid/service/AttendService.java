package com.aqinn.actmanagersysandroid.service;

import com.aqinn.actmanagersysandroid.data.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author Aqinn
 * @date 2020/12/25 10:09 AM
 */
public interface AttendService {

    @FormUrlEncoded
    @POST("attend/{userId}/{actId}")
    Observable<ApiResult> createAttend(@Path("userId") Long userId, @Path("actId") Long actId,
                                       @Field("time") String time, @Field("type") Integer type);

    @FormUrlEncoded
    @PUT("attend/{attendId}/time")
    Observable<ApiResult> editAttendTime(@Path("attendId") Long attendId, @Field("time") String time);

    @FormUrlEncoded
    @PUT("attend/{attendId}/type")
    Observable<ApiResult> editAttendType(@Path("attendId") Long attendId, @Field("type") Integer type);

    @PUT("attend/{attendId}/start")
    Observable<ApiResult> startAttend(@Path("attendId") Long attendId);

    @PUT("attend/{attendId}/stop")
    Observable<ApiResult> stopAttend(@Path("attendId") Long attendId);

}
