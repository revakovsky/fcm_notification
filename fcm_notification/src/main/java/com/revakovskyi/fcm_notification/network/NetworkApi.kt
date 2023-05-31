package com.revakovskyi.fcm_notification.network

import com.revakovskyi.fcm_notification.dto.ResponseDto
import com.revakovskyi.fcm_notification.utils.Constants.Main.GAD_ID
import com.revakovskyi.fcm_notification.utils.Constants.Requests.ADD_USER
import com.revakovskyi.fcm_notification.utils.Constants.Requests.BASE_URL
import com.revakovskyi.fcm_notification.utils.Constants.Requests.CONTENT_TYPE
import com.revakovskyi.fcm_notification.utils.Constants.Requests.UPDATE_TAG
import com.revakovskyi.fcm_notification.utils.Constants.Requests.UPDATE_TOKEN
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

internal interface NetworkApi {

    @Headers(CONTENT_TYPE)
    @POST(ADD_USER)
    fun addUser(@Body requestBody: RequestBody): Call<ResponseDto>


    @Headers(CONTENT_TYPE)
    @PUT(UPDATE_TOKEN)
    fun sendToken(
        @Path(GAD_ID) gadid: String,
        @Body requestBody: RequestBody
    ): Call<ResponseDto>


    @Headers(CONTENT_TYPE)
    @PUT(UPDATE_TAG)
    fun sendTag(
        @Path(GAD_ID) gadid: String,
        @Body requestBody: RequestBody
    ): Call<ResponseDto>


    companion object {
        fun create(): NetworkApi {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(NetworkApi::class.java)
        }
    }

}