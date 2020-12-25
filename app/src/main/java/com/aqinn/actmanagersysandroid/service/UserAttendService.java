package com.aqinn.actmanagersysandroid.service;

import com.aqinn.actmanagersysandroid.data.ApiResult;

import io.reactivex.Observable;
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

}
