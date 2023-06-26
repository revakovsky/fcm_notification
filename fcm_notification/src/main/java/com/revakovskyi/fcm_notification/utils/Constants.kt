package com.revakovskyi.fcm_notification.utils

object Constants {

    object Main {
        const val TOPIC_NAME = "Pin_Notifications"
        const val MEDIA_TYPE = "application/json"

        const val GAD_ID = "gadid"
        const val COUNTRY = "country"
        const val LANGUAGE = "language"
        const val APP_PACKAGE = "app_package"
        const val FCM_TOKEN = "fcmtoken"
        const val TAG = "tag"
    }

    object Requests {
        const val BASE_URL = "https://aff-demo-push.herokuapp.com/"
        const val CONTENT_TYPE = "Content-Type: application/json"

        const val ADD_USER = "adduser"
        const val UPDATE_TOKEN = "updatetoken/{gadid}"
        const val UPDATE_TAG = "/updatetag/{gadid}"
    }

    object NotificationBuilder {
        const val INTENT_ACTION = "com.amanotes.classicalpian.ACTION_NOTIFICATION_CLICK"
    }

    object ErrorsMessages {
        const val SUBSCRIBE_TO_THE_TOPIC =
            "Failed to SUBSCRIBE to the topic. The task wasn't successful. The reason -"
        const val EXCEPTION_WHILE_SUBSCRIBING_TO_THE_TOPIC =
            "An error occurred while SUBSCRIBING to the topic or Firebase initialization. The reason -"

        const val ADDING_USER_TO_THE_DB =
            "There was a problem with ADDING A USER to the FCM Notification SDK database. The reason -"
        const val EXCEPTION_WHILE_ADDING_USER_TO_THE_DB =
            "An error occurred while ADDING A USER to the FCM Notification SDK database. The reason -"

        const val SENDING_TOKEN_TO_THE_DB =
            "There was a problem with sending the TOKEN to the FCM Notification SDK database. The reason -"
        const val EXCEPTION_WHILE_SENDING_TOKEN_TO_THE_DB =
            "An error occurred while sending the TOKEN to the FCM Notification SDK database. The reason -"

        const val SENDING_TAG_TO_THE_DB =
            "There was a problem with sending the TAG to the FCM Notification SDK database. The reason -"
        const val EXCEPTION_WHILE_SENDING_TAG_TO_THE_DB =
            "An error occurred while sending the TAG to the FCM Notification SDK database. The reason -"
    }

}