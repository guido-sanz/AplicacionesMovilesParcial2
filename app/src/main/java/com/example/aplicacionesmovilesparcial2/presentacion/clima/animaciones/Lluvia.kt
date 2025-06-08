package com.example.aplicacionesmovilesparcial2.presentacion.clima.animaciones

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

@Composable
fun Lluvia() {
    val drops = remember {
        List(150) { Offset(Random.nextFloat() * 1000f, Random.nextFloat() * 2000f) }.toMutableStateList()
    }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { frameTimeNanos ->
                // Actualizar cada gota individualmente
                drops.replaceAll { drop ->
                    val newY = drop.y + 10f // Velocidad de caída
                    if (newY > 2000f) { // Si sale de la pantalla
                        Offset(Random.nextFloat() * 1000f, 0f) // Reiniciar solo esa gota arriba
                    } else {
                        Offset(drop.x, newY)
                    }
                }
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drops.forEach { drop ->
            drawLine(
                color = Color.White.copy(alpha = 0.5f),
                start = drop,
                end = Offset(drop.x, drop.y + 40f), // Largo de la gota
                strokeWidth = 3f                   // Grosor de la gota
            )
        }
    }
}