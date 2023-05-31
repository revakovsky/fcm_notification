package com.amanotes.classicalpian.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amanotes.classicalpian.R
import com.amanotes.classicalpian.game.mvp.GamePresenter
import com.amanotes.classicalpian.game.mvp.GameView
import com.amanotes.classicalpian.ui.elements.BackgroundImage
import com.amanotes.classicalpian.ui.elements.CardItem
import com.amanotes.classicalpian.ui.elements.CustomImageButton
import com.amanotes.classicalpian.ui.elements.ShowAlertDialog
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val gamePresenter = koinInject<GamePresenter>()
    var currentScore by remember { mutableStateOf(0) }
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        ShowAlertDialog(
            alertTitle = stringResource(id = R.string.clear),
            alertMessage = stringResource(id = R.string.clear_message),
            onPositiveClick = {
                gamePresenter.onClearScore()
                showClearDialog = false
            },
            onDismiss = { showClearDialog = false }
        )
    }

    LaunchedEffect(true) {
        gamePresenter.onGetMaxScore()
        gamePresenter.attachView(object : GameView {
            override fun showScore(score: Int) {
                currentScore = score
            }
        })
    }

    DisposableEffect(Unit) {
        onDispose { gamePresenter.onDestroy() }
    }

    Box(modifier = modifier.fillMaxSize()) {

        BackgroundImage(imageRes = R.drawable.bg_img2, alphaBlack = 0.4f)

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {

            CardItem(
                textContent = currentScore.toString(),
                modifier = modifier
            )

            Spacer(modifier = Modifier.height(200.dp))

        }

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {

                CustomImageButton(
                    onClick = { navController.popBackStack() },
                    contentDescriptionRes = R.string.back,
                    buttonTextRes = R.string.back,
                    modifier = modifier
                )

                CustomImageButton(
                    onClick = { showClearDialog = true },
                    contentDescriptionRes = R.string.clear,
                    buttonTextRes = R.string.clear,
                    modifier = modifier
                )

            }

        }

    }

}
