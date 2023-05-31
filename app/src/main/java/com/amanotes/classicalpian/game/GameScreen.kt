package com.amanotes.classicalpian.game

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amanotes.classicalpian.R
import com.amanotes.classicalpian.base.Screens
import com.amanotes.classicalpian.game.mvp.GamePresenter
import com.amanotes.classicalpian.game.mvp.GameView
import com.amanotes.classicalpian.ui.elements.BackgroundImage
import com.amanotes.classicalpian.ui.elements.CustomImageButton
import com.amanotes.classicalpian.ui.elements.RegularText
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val scope = rememberCoroutineScope()
    val gamePresenter = koinInject<GamePresenter>()
    val context = LocalContext.current

    var random1 by remember { mutableStateOf(1) }
    var random2 by remember { mutableStateOf(1) }
    var random3 by remember { mutableStateOf(1) }

    var imageSlot1 by remember { mutableStateOf(R.drawable.slot_image_1) }
    var imageSlot2 by remember { mutableStateOf(R.drawable.slot_image_2) }
    var imageSlot3 by remember { mutableStateOf(R.drawable.slot_image_3) }

    var score by remember { mutableStateOf(100) }
    var maxScore by remember { mutableStateOf(0) }
    var buttonClicked by remember { mutableStateOf(false) }
    val mainTag = R.drawable.slot_image_10

    LaunchedEffect(key1 = true) {
        gamePresenter.onGetMaxScore()
        gamePresenter.attachView(object : GameView {
            override fun showScore(score: Int) {
                maxScore = score
            }
        })
    }

    DisposableEffect(Unit) {
        onDispose { scope.cancel() }
    }

    Box(modifier = modifier.fillMaxSize()) {

        BackgroundImage(imageRes = R.drawable.bg_img4, alphaBlack = 0.4f)

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {

            CustomImageButton(
                imageRes = R.drawable.image_button_gum_3,
                onClick = { navController.popBackStack() },
                contentDescriptionRes = R.string.to_menu,
                buttonTextRes = R.string.menu,
                modifier = modifier
                    .width(100.dp)
                    .height(100.dp)
            )

        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.TopEnd,
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gold_coin),
                    contentDescription = stringResource(
                        id = R.string.gold_coin
                    )
                )

                Spacer(modifier = modifier.width(8.dp))

                RegularText(text = score.toString())
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxHeight()
            ) {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black),
                    elevation = CardDefaults.elevatedCardElevation(24.dp),
                    modifier = modifier
                        .padding(32.dp)
                ) {

                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .padding(16.dp)
                    ) {

                        Card(
                            colors = CardDefaults.elevatedCardColors(),
                            modifier = modifier.padding(end = 8.dp)
                        ) {

                            Image(
                                painter = painterResource(id = imageSlot1),
                                contentDescription = stringResource(id = R.string.slot_image),
                                modifier = modifier
                                    .width(80.dp)
                                    .height(80.dp)
                            )

                        }

                        Card(
                            colors = CardDefaults.elevatedCardColors(),
                            modifier = modifier.padding(end = 8.dp)
                        ) {

                            Image(
                                painter = painterResource(id = imageSlot2),
                                contentDescription = stringResource(id = R.string.slot_image),
                                modifier = modifier
                                    .width(80.dp)
                                    .height(80.dp)
                            )

                        }

                        Card(
                            colors = CardDefaults.elevatedCardColors(),
                            modifier = modifier
                        ) {

                            Image(
                                painter = painterResource(id = imageSlot3),
                                contentDescription = stringResource(id = R.string.slot_image),
                                modifier = modifier
                                    .width(80.dp)
                                    .height(80.dp)
                            )

                        }

                    }

                }

                Spacer(modifier = modifier.height(64.dp))

                CustomImageButton(
                    onClick = {
                        buttonClicked = true
                        score -= 10
                        showToast(context, "-10")

                        scope.launch {
                            delay(500)

                            repeat(10) {
                                delay(300)
                                random1 = (1..10).random()
                                random2 = (1..10).random()
                                random3 = (1..10).random()

                                imageSlot1 = getRandomImage(random1)
                                imageSlot2 = getRandomImage(random2)
                                imageSlot3 = getRandomImage(random3)
                            }

                            if (random1 == random2 && random2 == random3 && random1 == mainTag) {
                                score += 50
                                showToast(context, "BINGO!!! +50")
                            } else if (random1 == random2 && random2 == random3) {
                                score += 30
                                showToast(context, "Success! +30")
                            } else if (random1 == random2 || random2 == random3 || random1 == random3) {
                                score += 10
                                showToast(context, "Almost.. +10")
                            } else {
                                showToast(context, "Nothing")
                                if (score == 0) {
                                    delay(500)
                                    navController.navigate(Screens.ResultsScreen.route)
                                }
                            }

                            if (score > maxScore) {
                                maxScore = score
                                gamePresenter.saveMaxScore(maxScore)
                            }

                            buttonClicked = false
                        }
                    },
                    contentDescriptionRes = R.string.spin,
                    buttonTextRes = R.string.spin,
                    modifier = modifier,
                    isClickable = !buttonClicked
                )

            }

        }

    }

}

private fun showToast(context: Context, text: String) {
    var toast = Toast(context)
    toast.cancel()
    toast = makeText(context, text, Toast.LENGTH_SHORT)
    toast.show()
}

fun getRandomImage(random: Int): Int {
    return when (random) {
        1 -> R.drawable.slot_image_1
        2 -> R.drawable.slot_image_2
        3 -> R.drawable.slot_image_3
        4 -> R.drawable.slot_image_4
        5 -> R.drawable.slot_image_5
        6 -> R.drawable.slot_image_6
        7 -> R.drawable.slot_image_7
        8 -> R.drawable.slot_image_8
        9 -> R.drawable.slot_image_9
        10 -> R.drawable.slot_image_10
        else -> R.drawable.slot_image_1
    }
}
