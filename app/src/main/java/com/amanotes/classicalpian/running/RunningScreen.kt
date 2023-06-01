package com.amanotes.classicalpian.running

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.amanotes.classicalpian.R
import com.amanotes.classicalpian.ui.elements.BackgroundImage
import com.amanotes.classicalpian.ui.elements.ShowAlertDialog
import com.amanotes.classicalpian.ui.theme.Blue
import com.amanotes.classicalpian.ui.theme.Pink

@Composable
fun RunningScreen(
    modifier: Modifier = Modifier,
    isInternetEnabled: Boolean = true,
    onClickPositive: () -> Unit
) {

    var showInternetDialog by remember { mutableStateOf(false) }

    if (!isInternetEnabled) showInternetDialog = true

    if (showInternetDialog) {
        ShowAlertDialog(
            alertTitle = stringResource(id = R.string.no_internet_connection),
            alertMessage = stringResource(id = R.string.turn_on_the_internet),
            positiveButtonText = R.string.ok,
            cancelable = false,
            showDismissButton = false,
            onPositiveClick = {
                onClickPositive()
                showInternetDialog = false
            }
        )
    }

    BackgroundImage(imageRes = R.drawable.bg_img5, alphaBlack = 0.3f)

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LinearProgressIndicator(
            color = Pink,
            trackColor = Blue,
            modifier = modifier
        )

        Spacer(modifier = modifier.height(150.dp))

    }
}