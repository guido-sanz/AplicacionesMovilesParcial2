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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Soleado() {
    val infiniteTransition = rememberInfiniteTransition()

    // Rotación infinita de los rayos
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(x = size.width / 2, y = (size.height / 2.6).toFloat())

        // Sol (círculo)
        drawCircle(
            color = Color(0xFFFFD700),
            center = center,
            radius = 80f
        )

        // Rayos del sol
        for (i in 0 until 12) {
            val angle = Math.toRadians((i * 30f + rotation).toDouble())
            val startX = center.x + cos(angle) * 100f
            val startY = center.y + sin(angle) * 100f
            val endX = center.x + cos(angle) * 140f
            val endY = center.y + sin(angle) * 140f

            drawLine(
                color = Color(0xFFFFA500),
                start = Offset(startX.toFloat(), startY.toFloat()),
                end = Offset(endX.toFloat(), endY.toFloat()),
                strokeWidth = 4f
            )
        }
    }
}