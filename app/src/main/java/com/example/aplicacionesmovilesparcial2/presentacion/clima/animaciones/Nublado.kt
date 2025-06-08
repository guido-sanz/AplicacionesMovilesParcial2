package com.example.aplicacionesmovilesparcial2.presentacion.clima.animaciones

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun Nublado() {
    val infiniteTransition = rememberInfiniteTransition()

    val cloudOffset by infiniteTransition.animateFloat(
        initialValue = -500f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = (size.height / 2.7).toFloat()

        drawCircle(
            color = Color.LightGray.copy(alpha = 0.5f),
            radius = 100f,
            center = Offset(centerX + cloudOffset, centerY)
        )
        drawCircle(
            color = Color.Gray.copy(alpha = 0.5f),
            radius = 120f,
            center = Offset(centerX + cloudOffset + 150f, centerY + 20f)
        )
        drawCircle(
            color = Color.LightGray.copy(alpha = 0.5f),
            radius = 90f,
            center = Offset(centerX + cloudOffset + 300f, centerY)
        )
    }
}
