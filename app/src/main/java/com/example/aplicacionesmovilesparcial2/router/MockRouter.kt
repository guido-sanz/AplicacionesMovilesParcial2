package com.example.aplicacionesmovilesparcial2.router

class MockRouter(var ruta: String = ""): Router {

    override fun irACiudades() {
        ruta = "ciudades"
    }

    override fun irAClima(lat: Float, lon: Float, nombre: String) {
        ruta = "clima?lat=$lat&lon=$lon&nombre=${nombre}"
    }

}