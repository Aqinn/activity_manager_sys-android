package com.aqinn.actmanagersysandroid.service;

import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.entity.User;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author Aqinn
 * @date 2020/12/23 12:58 PM
 */
public interface UserService {


    //    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @Headers("Content-type:multipart/form-data;charset=UTF-8")
    @FormUrlEncoded
    @POST("user")
    Call<ApiResult> createUser(@Field("account") String account, @Field("pwd") String pwd,
                               @Field("name") String name, @Field("contact") String contact,
                               @Field("sex") Integer sex, @Field("intro") String intro);

    @FormUrlEncoded
    @PUT("user/{userId}")
    Call<ApiResult> editUser(@Path("userId") Long userId, @FieldMap Map<String, Object> params);

    @GET("user/{userId}")
    Call<ApiResult<User>> getUserById(@Path("userId") Long userId);

}
