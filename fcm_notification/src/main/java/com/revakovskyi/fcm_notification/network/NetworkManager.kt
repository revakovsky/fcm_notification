package com.revakovskyi.fcm_notification.network

import com.revakovskyi.fcm_notification.AppNotificationService
import com.revakovskyi.fcm_notification.NotificationManager
import com.revakovskyi.fcm_notification.dto.ResponseDto
import com.revakovskyi.fcm_notification.utils.Constants.ErrorsMessages.ADDING_USER_TO_THE_DB
import com.revakovskyi.fcm_notification.utils.Constants.ErrorsMessages.EXCEPTION_WHILE_ADDING_USER_TO_THE_DB
import com.revakovskyi.fcm_notification.utils.Constants.ErrorsMessages.EXCEPTION_WHILE_SENDING_TAG_TO_THE_DB
import com.revakovskyi.fcm_notification.utils.Constants.ErrorsMessages.EXCEPTION_WHILE_SENDING_TOKEN_TO_THE_DB
import com.revakovskyi.fcm_notification.utils.Constants.ErrorsMessages.SENDING_TAG_TO_THE_DB
import com.revakovskyi.fcm_notification.utils.Constants.ErrorsMessages.SENDING_TOKEN_TO_THE_DB
import com.revakovskyi.fcm_notification.utils.RequestType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class NetworkManager {

    private val networkApi = NetworkApi.create()
    private val requestBodyCreator = RequestBodyCreator()

    suspend fun addUserToDb(): Boolean =
        addUser(requestBodyCreator.getRequestBody(RequestType.USER))

    suspend fun sendTokenToDb(): Boolean {
        val token = AppNotificationService.provideToken()
        val tokenRequestBody = requestBodyCreator.getRequestBody(RequestType.TOKEN, token = token)
        return sendToken(tokenRequestBody)
    }

    suspend fun sendTagToDb(tag: String): Boolean =
        sendTag(requestBodyCreator.getRequestBody(RequestType.TAG, tag = tag))


    private suspend fun addUser(requestBody: RequestBody): Boolean {
        return suspendCoroutine { continuation ->
            try {
                val userApiCall = networkApi.addUser(requestBody)
                userApiCall.enqueue(object : Callback<ResponseDto> {

                    override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                        if (response.isSuccessful && response.body() != null) continuation.resume(true)
                        else continuation.resume(false)
                    }

                    override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                        println(ADDING_USER_TO_THE_DB + t.localizedMessage)
                        continuation.resume(false)
                    }
                })
            } catch (e: Exception) {
                println(EXCEPTION_WHILE_ADDING_USER_TO_THE_DB + e.localizedMessage)
                continuation.resume(false)
            }
        }
    }

    private suspend fun sendToken(tokenRequestBody: RequestBody): Boolean {
        return suspendCoroutine { continuation ->
            try {
                val tokenApiCall = networkApi.sendToken(NotificationManager.provideGadId(), tokenRequestBody)
                tokenApiCall.enqueue(object : Callback<ResponseDto> {

                    override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                        if (response.isSuccessful && response.body() != null) continuation.resume(true)
                        else continuation.resume(false)
                    }

                    override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                        println(SENDING_TOKEN_TO_THE_DB + t.localizedMessage)
                        continuation.resume(false)
                    }
                })
            } catch (e: Exception) {
                println(EXCEPTION_WHILE_SENDING_TOKEN_TO_THE_DB + e.localizedMessage)
                continuation.resume(false)
            }
        }
    }

    private suspend fun sendTag(requestBody: RequestBody): Boolean {
        return suspendCoroutine { continuation ->
            try {
                val tagApiCall = networkApi.sendTag(NotificationManager.provideGadId(), requestBody)
                tagApiCall.enqueue(object : Callback<ResponseDto> {

                    override fun onResponse(call: Call<ResponseDto>, response: Response<ResponseDto>) {
                        if (response.isSuccessful && response.body() != null) continuation.resume(true)
                        else continuation.resume(false)
                    }

                    override fun onFailure(call: Call<ResponseDto>, t: Throwable) {
                        println(SENDING_TAG_TO_THE_DB + t.localizedMessage)
                        continuation.resume(false)
                    }
                })
            } catch (e: Exception) {
                println(EXCEPTION_WHILE_SENDING_TAG_TO_THE_DB + e.localizedMessage)
                continuation.resume(false)
            }
        }
    }

}