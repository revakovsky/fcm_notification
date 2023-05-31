package com.amanotes.classicalpian.game

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.amanotes.classicalpian.R
import com.amanotes.classicalpian.base.Screens
import com.amanotes.classicalpian.ui.elements.BackgroundImage
import com.amanotes.classicalpian.ui.elements.CustomImageButton
import com.amanotes.classicalpian.ui.elements.ShowAlertDialog

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val context = LocalContext.current
    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        ShowAlertDialog(
            alertTitle = stringResource(id = R.string.exit_game),
            alertMessage = stringResource(id = R.string.exit_message),
            onPositiveClick = { ActivityCompat.finishAffinity(context as Activity) },
            onDismiss = { showExitDialog = false }
        )
    }

    BackHandler { showExitDialog = true }

    Box(modifier = modifier.fillMaxSize()) {

        BackgroundImage(imageRes = R.drawable.bg_img3, alphaBlack = 0.3f)

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {

                CustomImageButton(
                    onClick = { navController.navigate(Screens.GameScreen.route) },
                    contentDescriptionRes = R.string.start,
                    buttonTextRes = R.string.start,
                    modifier = modifier
                )

                CustomImageButton(
                    onClick = { navController.navigate(Screens.SettingsScreen.route) },
                    contentDescriptionRes = R.string.settings,
                    buttonTextRes = R.string.settings,
                    modifier = modifier
                )

                CustomImageButton(
                    onClick = { showExitDialog = true },
                    contentDescriptionRes = R.string.exit,
                    buttonTextRes = R.string.exit,
                    modifier = modifier
                )

            }

        }

    }
}