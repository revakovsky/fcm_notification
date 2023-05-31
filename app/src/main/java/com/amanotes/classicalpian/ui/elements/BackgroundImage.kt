package com.amanotes.classicalpian.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.amanotes.classicalpian.R

@Composable
fun BackgroundImage(
    modifier: Modifier = Modifier,
    imageRes: Int,
    alphaBlack: Float = 0.1f
) {

    Image(
        painter = painterResource(id = imageRes),
        contentDescription = stringResource(id = R.string.background_image),
        contentScale = ContentScale.Crop,
        modifier = modifier.fillMaxSize()
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = alphaBlack))
    )

}