package com.amanotes.classicalpian.running

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.Keep
import androidx.core.net.toUri
import com.amanotes.classicalpian.model.source.CryptoManager
import com.amanotes.classicalpian.model.source.FileManager
import com.amanotes.classicalpian.model.source.Loader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import java.io.File

@Keep
class UrlService : Service() {

    private val scope = CoroutineScope(Job() + Dispatchers.IO)

    private val cryptoManager by inject<CryptoManager>()
    private val fileManager by inject<FileManager>()
    private val loader by inject<Loader>()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val (referrerString, gadId) = extractDataFromIntent(intent)

        if (referrerString != null && gadId != null) {
            val encryptedData = cryptoManager.encryptStringData(referrerString, gadId)
            val file = fileManager.createCryptoFile(encryptedData.data)

            if (file != null) {
                scope.launch {
                    val filePart = getFilePart(file)
                    val campaign = loader.fetchCampaign(filePart)

                    if (campaign.isNotBlank()) sendUrlBroadcast(
                        url = buildUrl(gadId, campaign),
                        tag = campaign.substringBefore("/")
                    )
                    else sendUrlBroadcast()
                }
            } else sendUrlBroadcast()
        } else sendUrlBroadcast()

        return START_STICKY
    }

    private fun extractDataFromIntent(intent: Intent?): Pair<String?, String?> {
        val referrerString = intent?.getStringExtra(KEY_REFERRER)
        val gadId = intent?.getStringExtra(KEY_GADID)
        return Pair(referrerString, gadId)
    }

    private fun getFilePart(file: File): MultipartBody.Part {
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "fileToUpload",
                "file.txt",
                file.asRequestBody("text/plain".toMediaTypeOrNull())
            )
            .build()
        return multipartBody.part(0)
    }

    private fun buildUrl(gadId: String, campaign: String): String {
        val decryptedString =
            cryptoManager.encryptOrDecryptString("eyy}~7\"\"~zhhyobclcwl~#~o~\"lf\u007Fke{c~#}e}\"")

        return decryptedString.toUri().buildUpon().apply {
            appendQueryParameter("0pE492", gadId)
            appendQueryParameter("zH667z", campaign)
        }.toString()
    }

    private fun sendUrlBroadcast(url: String? = "", tag: String = "") {
        sendBroadcast(Intent(URL_INTENT_NAME).apply {
            putExtra(KEY_URL, url)
            putExtra(KEY_TAG, tag)
        })
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        private const val KEY_REFERRER = "referrer"
        private const val KEY_GADID = "gadid"
        const val URL_INTENT_NAME = "url_result"
        const val KEY_URL = "url"
        const val KEY_TAG = "tag"

        fun passReferrerAndGadId(referrerString: String, gadId: String) = Intent().apply {
            putExtra(KEY_REFERRER, referrerString)
            putExtra(KEY_GADID, gadId)
        }
    }

}