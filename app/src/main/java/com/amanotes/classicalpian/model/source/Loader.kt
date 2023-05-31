package com.amanotes.classicalpian.model.source

import android.util.Log
import okhttp3.MultipartBody
import java.io.IOException

interface Loader {

    suspend fun fetchCampaign(filePart: MultipartBody.Part): String

}

class LoaderImpl(private val apiService: ApiService) : Loader {

    override suspend fun fetchCampaign(filePart: MultipartBody.Part): String {
        return try {
            val response = apiService.uploadFile(filePart)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.campaign.toString()
            } else {
                Log.d("TAG_Max", "Request failed: ${response.code()} - ${response.errorBody()}")
                ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("TAG_Max", "Request failed: ${e.printStackTrace()}, ${e.message}")
            ""
        }
    }

}