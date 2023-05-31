package com.amanotes.classicalpian.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.amanotes.classicalpian.R

val Chewy = FontFamily(
    Font(R.font.chewy_regular, weight = FontWeight.Normal)
)

val Chewy_Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Chewy,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Chewy,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
    ),
)
