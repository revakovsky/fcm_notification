package com.amanotes.classicalpian.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first

class GameRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : GameRepository {

    private val scoreKey = "score"

    override suspend fun getScore(): Int? {
        val storeKey = intPreferencesKey(scoreKey)
        val preferences = dataStore.data.first()
        return preferences[storeKey]
    }

    override suspend fun saveScore(score: Int) {
        val storeKey = intPreferencesKey(scoreKey)
        dataStore.edit { mutablePreferences ->
            mutablePreferences[storeKey] = score
        }
    }

    override suspend fun clearScore() {
        val storeKey = intPreferencesKey(scoreKey)
        dataStore.edit { mutablePreferences ->
            mutablePreferences.remove(storeKey)
        }
    }

}