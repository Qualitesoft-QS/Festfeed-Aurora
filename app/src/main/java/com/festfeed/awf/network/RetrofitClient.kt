package com.festfeed.awf.network

import android.net.sip.SipErrorCode.TIME_OUT
import com.festfeed.awf.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    const val TIME_OUT = 2 * 60 * 1000

    private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor)
        .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS).build()

    val instance: Api by lazy{

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit.create(Api::class.java)
    }

}