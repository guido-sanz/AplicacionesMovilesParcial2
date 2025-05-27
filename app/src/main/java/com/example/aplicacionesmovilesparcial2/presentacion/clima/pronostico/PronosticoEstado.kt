package com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico

import ForecastHour


sealed class PronosticoEstado {
    data class Exitoso (
        val climas: List<ForecastHour>,
        ) : PronosticoEstado()
    data class Error(
        val mensaje :String = "",
    ) : PronosticoEstado()
    data object Vacio: PronosticoEstado()
    data object Cargando: PronosticoEstado()

}
