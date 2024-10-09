package com.example.apptemplates.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.apptemplates.robotoFamily
import com.example.apptemplates.sarabunFamily

object AppTextStyles {


    // Header Style

    fun headerStyle(textColor: Color) = TextStyle(
        fontSize = 42.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = sarabunFamily,
        color = textColor,
        textAlign = TextAlign.Center
    )


    // Sub Header Style

    fun subTextStyle(textColor: Color) = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = sarabunFamily,
        color = textColor,
        textAlign = TextAlign.Center
    )


    // Button Style

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

}