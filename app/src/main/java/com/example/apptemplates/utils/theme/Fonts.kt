package com.example.apptemplates.utils.theme


import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.apptemplates.R


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)


val montserratFont = GoogleFont("Montserrat")
val robotoFont = GoogleFont("Roboto")


val montserratFamily = FontFamily(
    Font(googleFont = montserratFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = montserratFont, fontProvider = provider, weight = FontWeight.Bold)
)

val robotoFamily = FontFamily(
    Font(googleFont = robotoFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = robotoFont, fontProvider = provider, weight = FontWeight.Bold)
)


val ralewayFont = GoogleFont("Raleway")
val sourceSansFont = GoogleFont("Source Sans Pro")


val ralewayFamily = FontFamily(
    Font(googleFont = ralewayFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = ralewayFont, fontProvider = provider, weight = FontWeight.Bold)
)

val sourceSansFamily = FontFamily(
    Font(googleFont = sourceSansFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = sourceSansFont, fontProvider = provider, weight = FontWeight.Bold)
)


val poppinsFont = GoogleFont("Poppins")
val latoFont = GoogleFont("Lato")


val poppinsFamily = FontFamily(
    Font(googleFont = poppinsFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = poppinsFont, fontProvider = provider, weight = FontWeight.Bold)
)

val latoFamily = FontFamily(
    Font(googleFont = latoFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = latoFont, fontProvider = provider, weight = FontWeight.Bold)
)


val nunitoFont = GoogleFont("Nunito")


val nunitoFamily = FontFamily(
    Font(googleFont = nunitoFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = nunitoFont, fontProvider = provider, weight = FontWeight.Bold)
)


val playfairFont = GoogleFont("Playfair Display")
val quicksandFont = GoogleFont("Quicksand")


val playfairFamily = FontFamily(
    Font(googleFont = playfairFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = playfairFont, fontProvider = provider, weight = FontWeight.Bold)
)

val rubikFont = GoogleFont("Rubik")


val rubikFamily = FontFamily(
    Font(googleFont = rubikFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = rubikFont, fontProvider = provider, weight = FontWeight.Bold)
)


val archivoBlackFont = GoogleFont("Archivo Black")


val archivoBlackFamily = FontFamily(
    Font(googleFont = archivoBlackFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = archivoBlackFont, fontProvider = provider, weight = FontWeight.Bold)
)


val sarabunFont = GoogleFont("Sarabun")


val sarabunFamily = FontFamily(
    Font(googleFont = sarabunFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = sarabunFont, fontProvider = provider, weight = FontWeight.Bold)
)


val montserrat = GoogleFont("Montserrat")
val montserratFontFamily = FontFamily(
    Font(googleFont = montserrat, fontProvider = provider, weight = FontWeight.W700),
    Font(googleFont = montserrat, fontProvider = provider, weight = FontWeight.W500)
)


val nunito = GoogleFont("Nunito")
val nunitoFontFamily = FontFamily(
    Font(googleFont = nunito, fontProvider = provider, weight = FontWeight.W600),
)
