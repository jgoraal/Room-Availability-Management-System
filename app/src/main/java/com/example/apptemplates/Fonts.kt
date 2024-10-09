package com.example.apptemplates


import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont

// GoogleFont provider
val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)


// Define Google Fonts for headers and buttons
val montserratFont = GoogleFont("Montserrat")
val robotoFont = GoogleFont("Roboto")

// Font families using GoogleFont.Provider
val montserratFamily = FontFamily(
    Font(googleFont = montserratFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = montserratFont, fontProvider = provider, weight = FontWeight.Bold)
)

val robotoFamily = FontFamily(
    Font(googleFont = robotoFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = robotoFont, fontProvider = provider, weight = FontWeight.Bold)
)


// Define Google Fonts for headers and buttons
val ralewayFont = GoogleFont("Raleway")
val sourceSansFont = GoogleFont("Source Sans Pro")

// Font families using GoogleFont.Provider
val ralewayFamily = FontFamily(
    Font(googleFont = ralewayFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = ralewayFont, fontProvider = provider, weight = FontWeight.Bold)
)

val sourceSansFamily = FontFamily(
    Font(googleFont = sourceSansFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = sourceSansFont, fontProvider = provider, weight = FontWeight.Bold)
)


// Define Google Fonts
val poppinsFont = GoogleFont("Poppins")
val latoFont = GoogleFont("Lato")

// Font families using GoogleFont.Provider
val poppinsFamily = FontFamily(
    Font(googleFont = poppinsFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = poppinsFont, fontProvider = provider, weight = FontWeight.Bold)
)

val latoFamily = FontFamily(
    Font(googleFont = latoFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = latoFont, fontProvider = provider, weight = FontWeight.Bold)
)

// Define Google Fonts
val nunitoFont = GoogleFont("Nunito")

// Font families using GoogleFont.Provider
val nunitoFamily = FontFamily(
    Font(googleFont = nunitoFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = nunitoFont, fontProvider = provider, weight = FontWeight.Bold)
)


// Define Google Fonts
val playfairFont = GoogleFont("Playfair Display")
val quicksandFont = GoogleFont("Quicksand")

// Font families using GoogleFont.Provider
val playfairFamily = FontFamily(
    Font(googleFont = playfairFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = playfairFont, fontProvider = provider, weight = FontWeight.Bold)
)

val rubikFont = GoogleFont("Rubik")

// Font family using GoogleFont.Provider for welcome message
val rubikFamily = FontFamily(
    Font(googleFont = rubikFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = rubikFont, fontProvider = provider, weight = FontWeight.Bold)
)


val archivoBlackFont = GoogleFont("Archivo Black")

// Font family using GoogleFont.Provider for welcome message
val archivoBlackFamily = FontFamily(
    Font(googleFont = archivoBlackFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = archivoBlackFont, fontProvider = provider, weight = FontWeight.Bold)
)


// Define Google Font for welcome message
val sarabunFont = GoogleFont("Sarabun")

// Font family using GoogleFont.Provider for welcome message
val sarabunFamily = FontFamily(
    Font(googleFont = sarabunFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = sarabunFont, fontProvider = provider, weight = FontWeight.Bold)
)
