package com.example.aplicacionesmovilesparcial2.router

import androidx.navigation.NavController

interface Router {
    fun irACiudades()
    fun irAClima(lat: Float, lon: Float, nombre: String)
}

class RouterImplementation(private val navController: NavController) : Router {

    override fun irACiudades() {
        navController.navigate("ciudades")
    }

    override fun irAClima(lat: Float, lon: Float, nombre: String) {
        val ruta = "clima?lat=$lat&lon=$lon&nombre=${nombre}"
        navController.navigate(ruta)
    }
}
