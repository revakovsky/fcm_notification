package com.revakovskyi.fcm_notification.network

import com.google.gson.Gson
import com.revakovskyi.fcm_notification.NotificationManager
import com.revakovskyi.fcm_notification.utils.Constants
import com.revakovskyi.fcm_notification.utils.Constants.Main.APP_PACKAGE
import com.revakovskyi.fcm_notification.utils.Constants.Main.COUNTRY
import com.revakovskyi.fcm_notification.utils.Constants.Main.FCM_TOKEN
import com.revakovskyi.fcm_notification.utils.Constants.Main.GAD_ID
import com.revakovskyi.fcm_notification.utils.Constants.Main.LANGUAGE
import com.revakovskyi.fcm_notification.utils.Constants.Main.TAG
import com.revakovskyi.fcm_notification.utils.RequestType
import okhttp3.MediaType
import okhttp3.RequestBody

internal class RequestBodyCreator {

    fun getRequestBody(
        requestType: RequestType,
        token: String = "",
        tag: String = "",
    ): RequestBody {
        val requestBody = when (requestType) {
            RequestType.USER -> getBodyParams(RequestType.USER)
            RequestType.TOKEN -> getBodyParams(RequestType.TOKEN, token = token)
            RequestType.TAG -> getBodyParams(RequestType.TAG, tag = tag)
        }
        val requestBodyJson = Gson().toJson(requestBody)
        return RequestBody.create(MediaType.parse(Constants.Main.MEDIA_TYPE), requestBodyJson)
    }

    private fun getBodyParams(
        requestType: RequestType,
        token: String = "",
        tag: String = "",
    ): Map<String, String> {
        return when (requestType) {
            RequestType.USER -> mapOf(
                GAD_ID to NotificationManager.provideGadId(),
                COUNTRY to NotificationManager.provideCountry(),
                LANGUAGE to NotificationManager.provideLanguage(),
                APP_PACKAGE to NotificationManager.provideAppPackage()
            )

            RequestType.TOKEN -> mapOf(
                FCM_TOKEN to token,
                APP_PACKAGE to NotificationManager.provideAppPackage()
            )

            RequestType.TAG -> mapOf(
                TAG to tag,
                APP_PACKAGE to NotificationManager.provideAppPackage()
            )
        }
    }

}