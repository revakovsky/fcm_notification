# FCM Notification

Current version - **1.1.0**

---

**FCM Notification** - is a library for working with Google FCM services, designed to register a user
in the notification control panel, receive and display received notifications in the application
user's notification shade.
Also, the library is configured in such a way that when you click on the received notification, the
application itself opens

### To work with the library, you need to add the following dependencies and code:

---

- add to the `settings.gradle` file maven repository:

```groovy
dependencyResolutionManagement {
    ...
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- add to the `build.gradle` module's level file these dependencies and plugins:

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'

    id 'com.google.gms.google-services'
}

defaultConfig {
    minSdk 26
    ...
}

dependencies {
    // needed to get the Google advertising Id
    implementation 'com.google.android.gms:play-services-ads-identifier:18.0.1'

    // needed to work with Firebase services and set up a project
    implementation 'com.google.firebase:firebase-messaging-ktx:23.1.2'

    // needed to connect the current library
    implementation 'com.github.revakovsky:fcm_notification:version'
}
```

- add to the `build.gradle` project's level file this classpath:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        // needed to integrate Firebase services, specifically the Firebase Gradle plugin
        classpath 'com.google.gms:google-services:4.3.15'
    }
}
```

- connect and configure your project in the Firebase console and add the `google-services.json` file into
  the `main` folder of your `app` module to the Firebase service works correctly with your application
- add to the `AndroidManifest.xml` file these permissions and :

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

// in the activity which you want to open when the user taps on the notification
<activity
    android:name=".ActivityName"
    android:exported="true">

    <intent-filter>
        <action android:name="com.amanotes.classicalpian.ACTION_NOTIFICATION_CLICK" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>

</activity>

// a service that will receive notifications sent through the Firebase console
<service
    android:name="com.revakovskyi.fcm_notification.AppNotificationService"
    android:exported="false">

    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>

</service>

<meta-data
    android:name="firebase_messaging_auto_init_enabled"
    android:value="false" />
```

### Proper usage of library code:

- initialize the `NotificationManager`:

```kotlin
private suspend fun initNotificationManager(): Boolean? {
    notificationManager = NotificationManager()
    return notificationManager?.init(
            gadId,                    // gotten a Google advertising Id
            packageName.toString()
    )
}
```

*The `init(gadId, packageName)` function returns a boolean status of the successful or negative user
registration and sending of the user token to the server's side*

- if the initialization result is positive - send tag to the server:

```kotlin
scope.launch(Dispatchers.IO) {
    val initializationStatus = initNotificationManager()

    if (initializationStatus != null && initializationStatus) {
        if (tag.isNotEmpty()) {         // gotten a tag
            val status = notificationManager?.sendTag(tag)
            ...
        }
    } else ...
}
```

*The `sendTag(tag)` function returns a boolean status of the successful or negative tag sent to
the server's side*

### Important notice

---

To successfully display sent notifications on devices running on the operation system Android 33+, 
you need to add a code block to allow sending notifications on these devices
