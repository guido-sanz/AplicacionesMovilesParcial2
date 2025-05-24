package com.example.aplicacionesmovilesparcial2.repository

import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad
import com.example.aplicacionesmovilesparcial2.repository.modelos.Clima
import com.example.aplicacionesmovilesparcial2.repository.modelos.ListForecast

interface Repositorio {
    suspend fun buscarCiudad(ciudad: String): List<Ciudad>
    suspend fun traerClima(lat: Float, lon: Float) : Clima
    suspend fun traerPronostico(nombre: String) : List<ListForecast>
}