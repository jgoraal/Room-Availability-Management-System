package com.example.apptemplates.presentation.login.login_menu.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay


//  Text Fade In Animation =========================================================================

@Composable
fun setupFadeInAnimations(
    durationMillis: Int = 1500,
    delayBetween: Int = 500
): Animatable<Float, AnimationVector1D> {
    val animation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(delayBetween.toLong())
        animation.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis)
        )
    }

    return animation
}

// =================================================================================================


//  Wave Text Animation ============================================================================

@Composable
fun WaveText(
    text: String,
    textStyle: TextStyle,
    delayBetweenLetters: Int = 100,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        text.forEachIndexed { index, char ->
            val animationDelay = remember { Animatable(0f) }
            LaunchedEffect(key1 = char) {
                delay((index * delayBetweenLetters).toLong())
                animationDelay.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
            Text(
                text = char.toString(),
                style = textStyle,
                modifier = Modifier.alpha(animationDelay.value)
            )
        }
    }
}

// =================================================================================================


//  Gradient Effect ================================================================================

@Composable
fun gradientsEffect(): Float {
    // Infinite transition for color shift
    val infiniteTransition = rememberInfiniteTransition(label = "GradientEffectTransition")

    // Animate the progress of color shift (0f to 1f)
    return infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GradientEffectAnimation"
    ).value
}

// =================================================================================================