package com.example.aplicacionesmovilesparcial2.preferencias

class PreferenciasMock : Preferencias {
    var ciudad: Triple<String, Float, Float>? = null

    override fun guardarCiudad(nombre: String, lat: Float, lon: Float) {
        ciudad = Triple(nombre, lat, lon)
    }

    override fun obtenerCiudad(): Triple<String, Float, Float>? {
        return ciudad
    }

    override fun borrarCiudad() {
        ciudad = null
    }
}
