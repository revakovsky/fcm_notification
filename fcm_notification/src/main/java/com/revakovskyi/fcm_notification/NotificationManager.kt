package com.revakovskyi.fcm_notification

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.revakovskyi.fcm_notification.network.NetworkManager
import com.revakovskyi.fcm_notification.utils.Constants.ErrorsMessages.EXCEPTION_WHILE_SUBSCRIBING_TO_THE_TOPIC
import com.revakovskyi.fcm_notification.utils.Constants.ErrorsMessages.SUBSCRIBE_TO_THE_TOPIC
import com.revakovskyi.fcm_notification.utils.Constants.Main.TOPIC_NAME
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Locale

class NotificationManager {

    private val networkManager = NetworkManager()
    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun init(gadId: String, appsPackage: String): Boolean {
        val deferred = CompletableDeferred<Boolean>()
        initParameters(gadId, appsPackage)
        initFCM(deferred)
        return deferred.await()
    }

    suspend fun sendTag(tag: String): Boolean {
        val deferred = CompletableDeferred<Boolean>()
        if (isTokenAdded) {
            val sendingTagStatus = networkManager.sendTagToDb(tag)
            if (sendingTagStatus) deferred.complete(true)
            else deferred.complete(false)
        } else deferred.complete(false)
        scope.cancel()
        return deferred.await()
    }


    private fun initParameters(gadId: String, appsPackage: String) {
        passedGadId = gadId
        country = Locale.getDefault().country.toString()
        language = Locale.getDefault().language.toString()
        passedAppPackage = appsPackage
    }

    private fun initFCM(deferred: CompletableDeferred<Boolean>) {
        try {
            Firebase.messaging
                .subscribeToTopic(TOPIC_NAME)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Firebase.messaging.isAutoInitEnabled = true
                        sendDataToServer(deferred)
                    } else {
                        println(SUBSCRIBE_TO_THE_TOPIC + task.exception)
                        deferred.complete(false)
                    }
                }
        } catch (e: Exception) {
            println(EXCEPTION_WHILE_SUBSCRIBING_TO_THE_TOPIC + e.localizedMessage)
            deferred.complete(false)
        }
    }

    private fun sendDataToServer(deferred: CompletableDeferred<Boolean>) {
        scope.launch {
            val addingUserStatus = networkManager.addUserToDb()
            if (addingUserStatus) {
                val sendingTokenStatus = networkManager.sendTokenToDb()
                if (sendingTokenStatus) {
                    isTokenAdded = true
                    deferred.complete(true)

                } else deferred.complete(false)
            } else deferred.complete(false)
        }
    }

    companion object {
        private var passedGadId = ""
        fun provideGadId(): String = passedGadId

        private var country = ""
        fun provideCountry(): String = country

        private var language = ""
        fun provideLanguage(): String = language

        private var passedAppPackage = ""
        fun provideAppPackage(): String = passedAppPackage

        private var isTokenAdded = false
    }

}