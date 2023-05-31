package com.amanotes.classicalpian.ui.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanotes.classicalpian.ui.theme.Chewy_Typography
import com.amanotes.classicalpian.ui.theme.Red

@Composable
fun RegularText(
    text: String,
    textAlign: TextAlign = TextAlign.Center,
    textColor: Color = Color.White,
    textStyle: TextStyle = Chewy_Typography.bodyLarge,
    shadowColor: Color = Color.Black,
    shadowRadius: Float = 3f,
    shadowOffsetX: Float = 2f,
    shadowOffsetY: Float = 2f,
    letterSpacing: TextUnit = 1.sp,
    lineHeight: TextUnit = 30.sp
) {

    Text(
        text = text,
        textAlign = textAlign,
        color = textColor,
        style = textStyle.copy(
            shadow = Shadow(
                color = shadowColor,
                blurRadius = shadowRadius,
                offset = Offset(shadowOffsetX, shadowOffsetY)
            )
        ),
        letterSpacing = letterSpacing,
        lineHeight = lineHeight
    )

}


@Composable
fun TitleText(
    textRes: Int,
    textAlign: TextAlign = TextAlign.Center,
    textColor: Color = Red,
    textStyle: TextStyle = Chewy_Typography.titleLarge,
    shadowColor: Color = Color.Black,
    shadowRadius: Float = 25f,
    shadowOffsetX: Float = 2f,
    shadowOffsetY: Float = 2f,
    letterSpacing: TextUnit = 1.sp,
    lineHeight: TextUnit = 30.sp
) {

    Text(
        text = stringResource(id = textRes),
        textAlign = textAlign,
        color = textColor,
        style = textStyle.copy(
            shadow = Shadow(
                color = shadowColor,
                blurRadius = shadowRadius,
                offset = Offset(shadowOffsetX, shadowOffsetY)
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        letterSpacing = letterSpacing,
        lineHeight = lineHeight
    )

}


@Composable
fun ButtonText(
    textRes: Int,
    color: Color = Color.White,
    style: TextStyle = Chewy_Typography.titleLarge,
    shadowColor: Color = Color.Black,
    shadowRadius: Float = 0f,
    shadowOffsetX: Float = 0f,
    shadowOffsetY: Float = 0f,
    letterSpacing: TextUnit = 0.sp
) {

    Text(
        text = stringResource(id = textRes),
        color = color,
        style = style.copy(
            shadow = Shadow(
                color = shadowColor,
                blurRadius = shadowRadius,
                offset = Offset(shadowOffsetX, shadowOffsetY)
            )
        ),
        letterSpacing = letterSpacing
    )

}
