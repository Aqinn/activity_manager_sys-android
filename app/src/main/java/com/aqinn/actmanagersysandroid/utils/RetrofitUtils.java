package com.aqinn.actmanagersysandroid.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Aqinn
 * @date 2020/12/25 9:11 AM
 */
public class RetrofitUtils {

    private static final String TAG = "RetrofitUtils";

    private static final String BASE_URL = "http://10.16.97.117:8080/";
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getOkHttpClient())
            .build();

    /**
     * 获取 OkHttpClient
     * 可以用于打印请求参数的日志
     * （因为使用了 Rxjava + Retrofit 所以不可以直接打印 Request 的参数）
     * @return
     */
    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NotNull String s) {
                Log.d(TAG, "log: Http 参数: " + s);
            }
        });
        loggingInterceptor.setLevel(level);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        return builder.build();
    }

    public static Retrofit getRetrofit() {
        return retrofit;
    }

}
