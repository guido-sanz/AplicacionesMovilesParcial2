package com.example.aplicacionesmovilesparcial2.presentacion.clima

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaEstado
import com.example.aplicacionesmovilesparcial2.presentacion.clima.animaciones.Lluvia
import com.example.aplicacionesmovilesparcial2.presentacion.clima.animaciones.Nublado
import com.example.aplicacionesmovilesparcial2.presentacion.clima.animaciones.Soleado

@Composable
fun ClimaBackground(climaEstado: ClimaEstado) {
    val brush = when (climaEstado) {
        is ClimaEstado.Exitoso -> getBackgroundBrushForWeather(climaEstado.descripcion)
        else -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE0C3FC),
                Color(0xFF8EC5FC)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
    ) {
        when (climaEstado) {
            is ClimaEstado.Exitoso -> {
                when {
                    climaEstado.descripcion.contains("rain", ignoreCase = true) -> Lluvia()
                    climaEstado.descripcion.contains("cloud", ignoreCase = true) -> Nublado()
                    climaEstado.descripcion.contains("clear", ignoreCase = true) -> Soleado()
                }
            }
            else -> {

            }
        }
    }
}

fun getBackgroundBrushForWeather(weatherDescription: String): Brush {
    return when {
        weatherDescription.contains("rain", ignoreCase = true) -> {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF536976),
                    Color(0xFF292E49)
                )
            )
        }
        weatherDescription.contains("clear", ignoreCase = true) -> {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF56CCF2),
                    Color(0xFF2F80ED)
                )
            )
        }
        weatherDescription.contains("cloud", ignoreCase = true) -> {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF757F9A),
                    Color(0xFFD7DDE8)
                )
            )
        }
        else -> {
            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFE0C3FC),
                    Color(0xFF8EC5FC)
                )
            )
        }
    }
}