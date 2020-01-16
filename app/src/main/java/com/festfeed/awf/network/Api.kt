package com.festfeed.awf.network

import com.festfeed.awf.models.EventResponse
import com.festfeed.awf.models.LoginResponse
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("/user/register/device_id")
    fun createUser(
        @Body rawJsonString: String
    ): Call<LoginResponse>

    @POST("/auth/login/device_id")
    fun loginUser(
        @Body rawJsonString: String
    ): Call<LoginResponse>

    @GET("/event")
    fun getEvents(@Header("X-Api-Key") auth: String): Call<List<EventResponse>>

    @GET("/event")
    fun getEventsJsonElement(@Header("X-Api-Key") auth: String): Call<JsonElement>


    @POST("/user/fcm_token")
    fun registerToken(@Header("X-Api-Key") auth: String, @Body rawJsonString: String): Call<JsonElement>




}