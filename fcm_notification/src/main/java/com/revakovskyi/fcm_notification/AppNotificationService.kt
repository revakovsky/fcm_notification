package com.revakovskyi.fcm_notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.revakovskyi.fcm_notification.utils.Constants.NotificationBuilder.INTENT_ACTION
import com.revakovskyi.fcm_notification.utils.Constants.NotificationBuilder.NOTIFICATION_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

internal class AppNotificationService : FirebaseMessagingService() {

    private var channelId = ""
    private var channelName = ""
    private var descriptionText = ""

    override fun onNewToken(token: String) {
        firebaseToken = token
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()) {
            Log.d(
                "TAG_Max",
                "Message data payload: ${message.data}"
            )   // TODO: delete this block later
        }
        message.notification?.let {
            Log.d("TAG_Max", "Message Notification Body: ${it.body}")

            val title = it.title ?: ""
            val body = it.body ?: ""
            showNotification(title, body)
        }

    }

    @SuppressLint("MissingPermission")
    private fun showNotification(messageTitle: String, messageBody: String) {
        initValues()
        val pendingIntent = createPendingIntent()
        val notificationManager = provideNotificationManager()
        createNotificationChannel(notificationManager)

        val notification =
            if (pendingIntent != null) createNotification(
                messageTitle,
                messageBody,
                pendingIntent
            )
            else createNotification(messageTitle, messageBody)

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun initValues() {
        channelId = getString(R.string.default_notification_channel_id)
        channelName = getString(R.string.default_notification_channel_name)
        descriptionText = getString(R.string.channel_description)
    }

    private fun createPendingIntent(): PendingIntent? {
        val intent = Intent(INTENT_ACTION).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        return PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun provideNotificationManager(): NotificationManager =
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = descriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(
        messageTitle: String,
        messageBody: String,
        pendingIntent: PendingIntent? = null
    ): Notification {
        return if (pendingIntent != null) createNotificationWithPendingIntent(
            messageTitle,
            messageBody,
            pendingIntent
        )
        else createNotificationWithoutPendingIntent(messageTitle, messageBody)
    }

    private fun createNotificationWithPendingIntent(
        messageTitle: String,
        messageBody: String,
        pendingIntent: PendingIntent?
    ) = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(R.drawable.circle_notifications)
        .setColor(resources.getColor(R.color.notification_color, theme))
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        .setStyle(NotificationCompat.BigTextStyle().bigText(messageTitle))
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .build()

    private fun createNotificationWithoutPendingIntent(
        messageTitle: String,
        messageBody: String
    ) = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(R.drawable.circle_notifications)
        .setColor(resources.getColor(R.color.notification_color, theme))
        .setContentTitle(messageTitle)
        .setContentText(messageBody)
        .setStyle(NotificationCompat.BigTextStyle().bigText(messageTitle))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .build()

    companion object {
        private var firebaseToken = ""
        fun provideToken(): String = firebaseToken
    }

}