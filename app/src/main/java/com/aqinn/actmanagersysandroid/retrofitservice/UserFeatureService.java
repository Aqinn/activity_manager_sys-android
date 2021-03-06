package com.aqinn.actmanagersysandroid.retrofitservice;

import com.aqinn.actmanagersysandroid.data.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author Aqinn
 * @date 2021/1/15 4:26 PM
 */
public interface UserFeatureService {

    @FormUrlEncoded
    @POST("userfeature/{userId}")
    Observable<ApiResult> collectFace(
            @Path("userId") Long userId
            , @Field("f1") String f1
            , @Field("f2") String f2
            , @Field("f3") String f3
            , @Field("f4") String f4);

    @FormUrlEncoded
    @POST("/userfeature/check/{attendId}/{userId}")
    Observable<ApiResult> selfFaceRecognize(@Path("attendId") Long attendId, @Path("userId") Long userId,
                                            @Field("feature") String feature);

    @FormUrlEncoded
    @POST("/userfeature/videocheck")
    Observable<ApiResult> videoFaceRecognize(@Field("attendId") Long attendId,
                                             @Field("feature") String feature);

}
