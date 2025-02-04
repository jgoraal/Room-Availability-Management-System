package com.example.apptemplates.utils.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

object AppTextStyles {


    fun headerStyle(textColor: Color) = TextStyle(
        fontSize = 42.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = sarabunFamily,
        color = textColor,
        textAlign = TextAlign.Center
    )


    fun subTextStyle(textColor: Color) = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = sarabunFamily,
        color = textColor,
        textAlign = TextAlign.Center
    )


    fun buttonStyle(textColor: Color) = TextStyle(
        fontSize = 16.sp,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Bold,
        color = textColor,
        textAlign = TextAlign.Center
    )


    fun signInHeader(textColor: Color) = TextStyle(
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = sarabunFamily,
        color = textColor,
    )


    fun labelStyle(textColor: Color) = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Bold,
        color = textColor,
    )

    fun hintStyle(textColor: Color) = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Normal,
        color = textColor,
    )

    fun dialogTitleStyle(textColor: Color) = TextStyle(
        fontSize = 30.sp,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Bold,
        color = textColor,
    )

    fun dialogTextStyle(textColor: Color) = TextStyle(
        fontSize = 18.sp,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Normal,
        color = textColor,
    )

    fun errorStyle(errorColor: Color) = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoFamily,
        fontWeight = FontWeight.Bold,
        color = errorColor,
        textAlign = TextAlign.Start
    )


    fun topBarTitleStyle(textColor: Color) = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = montserratFontFamily,
        color = textColor,
    )

    fun topBarSubtitleStyle(textColor: Color) = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Light,
        fontFamily = montserratFontFamily,
        color = textColor,
    )

}