package com.amanotes.classicalpian.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amanotes.classicalpian.R
import com.amanotes.classicalpian.base.Screens
import com.amanotes.classicalpian.ui.elements.BackgroundImage
import com.amanotes.classicalpian.ui.elements.CustomImageButton
import com.amanotes.classicalpian.ui.elements.TitleText

@Composable
fun ResultsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    BackHandler { navController.popBackStack(Screens.MenuScreen.route, inclusive = false) }

    BackgroundImage(imageRes = R.drawable.bg_img1, alphaBlack = 0.4f)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(),
            elevation = CardDefaults.outlinedCardElevation(24.dp),
            modifier = modifier
                .padding(32.dp)
        ) {

            TitleText(
                textRes = R.string.you_ran_out_money,
                textColor = Color.Magenta,
                shadowRadius = 0f
            )

        }

        Spacer(modifier = modifier.height(120.dp))

        CustomImageButton(
            onClick = {
                navController.popBackStack(
                    Screens.MenuScreen.route,
                    inclusive = false
                )
            },
            contentDescriptionRes = R.string.to_menu,
            buttonTextRes = R.string.to_menu,
            modifier = modifier
        )

    }

}