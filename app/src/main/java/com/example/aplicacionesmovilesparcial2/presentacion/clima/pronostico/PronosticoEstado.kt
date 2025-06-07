package com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico

import ForecastDay


sealed class PronosticoEstado {
    data class Exitoso (
        val climas: List<ForecastDay>,
        ) : PronosticoEstado()
    data class Error(
        val mensaje :String = "",
    ) : PronosticoEstado()
    data object Vacio: PronosticoEstado()
    data object Cargando: PronosticoEstado()

}
