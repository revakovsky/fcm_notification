package com.amanotes.classicalpian.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanotes.classicalpian.R
import com.amanotes.classicalpian.ui.theme.Chewy_Typography

@Composable
fun CustomImageButton(
    modifier: Modifier,
    onClick: () -> Unit,
    buttonWidth: Int = 160,
    buttonHeight: Int = 85,
    imageRes: Int = R.drawable.image_button_gum_2,
    contentDescriptionRes: Int,
    contentScaleType: ContentScale = ContentScale.Fit,
    isClickable: Boolean = true,
    buttonTextRes: Int,
    textColor: Color = Color.Red,
    textStyle: TextStyle = Chewy_Typography.titleLarge,
    textShadowColor: Color = Color.Black,
    textShadowRadius: Float = 25f,
    textShadowOffsetX: Float = 2f,
    textShadowOffsetY: Float = 2f,
    textLetterSpacing: TextUnit = 1.sp
) {
    Box(
        modifier = modifier.size(buttonWidth.dp, buttonHeight.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { onClick() },
            enabled = isClickable,
            modifier = modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = stringResource(id = contentDescriptionRes),
                contentScale = contentScaleType,
                modifier = modifier
                    .width(buttonWidth.dp)
                    .height(buttonHeight.dp)
            )
        }

        ButtonText(
            textRes = buttonTextRes,
            color = textColor,
            style = textStyle,
            shadowColor = textShadowColor,
            shadowRadius = textShadowRadius,
            shadowOffsetX = textShadowOffsetX,
            shadowOffsetY = textShadowOffsetY,
            letterSpacing = textLetterSpacing,
        )

    }
}
