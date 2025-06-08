package com.example.aplicacionesmovilesparcial2.repository

import ForecastHour
import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad
import com.example.aplicacionesmovilesparcial2.repository.modelos.Clima

interface Repositorio {
    suspend fun buscarCiudad(ciudad: String): List<Ciudad>
    suspend fun traerClima(lat: Float, lon: Float) : Clima
    suspend fun traerPronostico(lat: Float, lon: Float) : List<ForecastHour>
    suspend fun buscarCiudadPorLatLon(lat: Double, lon: Double): Ciudad?
}