package com.example.aplicacionesmovilesparcial2.preferencias

import android.content.Context
import android.content.SharedPreferences

class PreferenciasUsuario(context: Context) : Preferencias {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("config_usuario", Context.MODE_PRIVATE)

    override  fun guardarCiudad(nombre: String, lat: Float, lon: Float) {
        prefs.edit().apply {
            putString("nombre", nombre)
            putFloat("lat", lat)
            putFloat("lon", lon)
            apply()
        }
    }

    override fun obtenerCiudad(): Triple<String, Float, Float>? {
        val nombre = prefs.getString("nombre", null)
        val lat = prefs.getFloat("lat", Float.NaN)
        val lon = prefs.getFloat("lon", Float.NaN)
        return if (nombre != null && !lat.isNaN() && !lon.isNaN()) {
            Triple(nombre, lat, lon)
        } else {
            null
        }
    }

    override fun borrarCiudad() {
        prefs.edit().clear().apply()
    }
}
