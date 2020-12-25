package com.aqinn.actmanagersysandroid.service;


import com.aqinn.actmanagersysandroid.data.ApiResult;
import com.aqinn.actmanagersysandroid.entity.show.ActIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.CreateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.ParticipateAttendIntroItem;
import com.aqinn.actmanagersysandroid.entity.show.UserDesc;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author Aqinn
 * @date 2020/12/25 5:33 PM
 */
public interface ShowItemService {

    @GET("setup/{userId}/userdesc")
    Observable<ApiResult<UserDesc>> getUserDesc(@Path("userId") Long userId);

    @GET("setup/{userId}/aii")
    Observable<ApiResult<List<ActIntroItem>>> getActIntroItem(@Path("userId") Long userId);

    @GET("setup/{userId}/caii")
    Observable<ApiResult<List<CreateAttendIntroItem>>> getCreateAttendIntroItem(@Path("userId") Long userId);

    @GET("setup/{userId}/paii")
    Observable<ApiResult<List<ParticipateAttendIntroItem>>> getParticipateAttendIntroItem(@Path("userId") Long userId);

}
