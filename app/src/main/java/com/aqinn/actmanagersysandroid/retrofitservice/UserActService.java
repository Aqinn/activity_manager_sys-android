package com.aqinn.actmanagersysandroid.retrofitservice;

import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Aqinn
 * @date 2020/12/25 10:32 AM
 */
public interface UserActService {

    @FormUrlEncoded
    @POST("useract/{userId}")
    Observable<ApiResult<ActIntroItem>> userJoinAct(@Path("userId") Long userId, @Field("code") Long code,
                                                    @Field("pwd") Long pwd);

    @DELETE("useract/{userId}/{actId}")
    Observable<ApiResult> userQuitAct(@Path("userId") Long userId, @Path("actId") Long actId);

}
