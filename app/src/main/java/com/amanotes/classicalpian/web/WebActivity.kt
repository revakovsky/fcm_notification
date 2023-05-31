package com.amanotes.classicalpian.web

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.amanotes.classicalpian.ui.theme.SweetBonanzaTheme

class WebActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(KEY_URL) ?: ""
        val isFirst = intent.getBooleanExtra(KEY_FIRST, true)

        setContent {
            SweetBonanzaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WebScreen(passedUrl = url, isFirst = isFirst)
                }
            }
        }
    }

    companion object {
        private const val KEY_URL = "Url"
        private const val KEY_FIRST = "First"

        fun passData(url: String, isFirst: Boolean) = Intent().apply {
            putExtra(KEY_URL, url)
            putExtra(KEY_FIRST, isFirst)
        }
    }

}