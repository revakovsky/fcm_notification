package com.amanotes.classicalpian.model

interface MainRepository {

    suspend fun getRunningData(): Pair<String?, Boolean?>
    suspend fun saveRunningData(url: String, urlSavingStatus: Boolean)
    suspend fun getReferrerString(): String
    suspend fun getGadId(): String

}