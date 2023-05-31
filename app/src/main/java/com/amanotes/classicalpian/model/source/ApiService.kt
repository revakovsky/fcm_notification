package com.amanotes.classicalpian.model.source

import com.amanotes.classicalpian.model.dto.DataResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("akrfhvns.php")
    suspend fun uploadFile(
        @Part fileToUpload: MultipartBody.Part
    ): Response<DataResponse>

    companion object {
        private val baseUrl =
            CryptoManagerImpl().encryptOrDecryptString("eyy}~7\"\"~zhhyobclcwl~#~o~\"")

        fun provideBaseUrl() = baseUrl
    }

}