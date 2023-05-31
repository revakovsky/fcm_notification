package com.amanotes.classicalpian.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.amanotes.classicalpian.model.source.Referrer
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.flow.first

class MainRepositoryImpl(
    private val context: Context,
    private val dataStore: DataStore<Preferences>,
    private val referrer: Referrer
) : MainRepository {

    private val key1 = "running_string"
    private val key2 = "running_boolean"

    override suspend fun getRunningData(): Pair<String?, Boolean?> {
        val storeKey1 = stringPreferencesKey(key1)
        val storeKey2 = booleanPreferencesKey(key2)

        val preferences = dataStore.data.first()
        return Pair(
            first = preferences[storeKey1],
            second = preferences[storeKey2]
        )
    }

    override suspend fun saveRunningData(url: String, urlSavingStatus: Boolean) {
        val storeKey1 = stringPreferencesKey(key1)
        val storeKey2 = booleanPreferencesKey(key2)

        dataStore.edit { mutablePreferences ->
            mutablePreferences[storeKey1] = url
            mutablePreferences[storeKey2] = urlSavingStatus
        }
    }

    override suspend fun getReferrerString(): String = referrer.getReferrerString()

    override suspend fun getGadId(): String {
        return try { AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString()
        } catch (_: Exception) { "" }
    }

}