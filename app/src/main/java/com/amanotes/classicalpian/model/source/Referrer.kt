package com.amanotes.classicalpian.model.source

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse.OK
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE
import com.android.installreferrer.api.InstallReferrerStateListener
import kotlinx.coroutines.CompletableDeferred

class Referrer(private val context: Context) {

    suspend fun getReferrerString(): String {
        val deferred = CompletableDeferred<String>()
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {

            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    OK -> {
                        val response = referrerClient.installReferrer
                        deferred.complete(response.installReferrer)
                    }

                    FEATURE_NOT_SUPPORTED -> deferred.complete("")
                    SERVICE_UNAVAILABLE -> deferred.complete("")
                }
                referrerClient.endConnection()
            }

            override fun onInstallReferrerServiceDisconnected() {
                deferred.complete("")
                referrerClient.endConnection()
            }

        })
        return deferred.await()
    }

}