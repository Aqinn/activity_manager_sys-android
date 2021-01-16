package com.aqinn.actmanagersysandroid.retrofitservice;


import com.aqinn.actmanagersysandroid.data.ApiResult;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author Aqinn
 * @date 2020/12/24 9:28 PM
 */
public interface ActService {

    //@Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("act")
    Observable<ApiResult> createAct(@Field("uId") Long uId, @Field("name") String name,
                                    @Field("desc") String desc, @Field("location") String location,
                                    @Field("time") String time);

    @FormUrlEncoded
    @PUT("act/{actId}")
    Observable<ApiResult> editAct(@Path("actId") Long actId, @FieldMap Map<String, Object> params);

    @PUT("act/{actId}/start")
    Observable<ApiResult> startAct(@Path("actId") Long actId);

    @PUT("act/{actId}/stop")
    Observable<ApiResult> stopAct(@Path("actId") Long actId);

}
