package com.example.apptemplates

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopBarPreview() {

    // Google Font Provider Setup
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    // Font for the app title (Montserrat Bold)
    val montserrat = GoogleFont("Montserrat")
    val montserratFontFamily = FontFamily(
        Font(
            googleFont = montserrat,
            fontProvider = provider,
            weight = FontWeight.W700, // Bold weight for impact
        )
    )

    // Font for the date (Nunito Light)
    val nunito = GoogleFont("Nunito")
    val nunitoFontFamily = FontFamily(
        Font(
            googleFont = nunito,
            fontProvider = provider,
            weight = FontWeight.W600, // Light weight for subtle elegance
        )
    )

    // For animated gradient
    val infiniteTransition = rememberInfiniteTransition()
    val animatedBrush = infiniteTransition.animateColor(
        initialValue = Color(0xFF512DA8),
        targetValue = Color(0xFF9575CD),
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    TopAppBar(
        modifier = Modifier
            .background(Color.Transparent) // Transparent to allow gradient
            .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp)) // Rounded bottom corners
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(animatedBrush.value, Color(0xFF9575CD),Color.Red,Color.Yellow) // Animated gradient
                )
            )
            .padding(horizontal = 16.dp),
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent), // Transparent container for a clean glassmorphism effect

        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // App Name with Animated Visibility
                    AnimatedVisibility(
                        visible = true, // You can add condition for dynamic visibility
                        enter = fadeIn(tween(5000)),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = "Roomy", // App Name
                            fontFamily = montserratFontFamily, // Use Montserrat Bold
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp, // Slightly larger and bolder for more impact
                            style = TextStyle(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFF3F0F0),
                                        Color(0xFFBDBABA)
                                    ),
                                    tileMode = TileMode.Mirror
                                )
                            ), // Bright white for clarity
                        )
                    }
                    // Date with Modern, Rounded Font and Gradient Color
                    AnimatedVisibility(
                        visible = true, // You can add condition for dynamic visibility
                        enter = slideInVertically(initialOffsetY = { -40 }) + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Text(
                            text = "Piątek, 27 września", // Elegant date format
                            fontFamily = nunitoFontFamily, // Use Nunito Light for softness
                            fontWeight = FontWeight.Light, // Light weight for a balanced look
                            fontSize = 18.sp, // Easy-to-read size
                            color = Color.White, // Slight contrast to stand out
                        )
                    }
                }
            }
        },

        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Menu,
                    contentDescription = "TopBar Menu",
                    tint = Color.White // White to match the theme
                )
            }
        },

        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "TopBar Account",
                    tint = Color.White // White to blend with the background
                )
            }
        }
    )
}