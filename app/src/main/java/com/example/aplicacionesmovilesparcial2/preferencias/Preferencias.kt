package com.example.aplicacionesmovilesparcial2.preferencias

interface Preferencias {
    fun guardarCiudad(nombre: String, lat: Float, lon: Float)
    fun obtenerCiudad(): Triple<String, Float, Float>?
    fun borrarCiudad()
}