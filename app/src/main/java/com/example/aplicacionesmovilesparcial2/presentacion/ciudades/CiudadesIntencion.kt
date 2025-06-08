package com.example.aplicacionesmovilesparcial2.presentacion.ciudades

import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad

sealed class CiudadesIntencion {
    data class Buscar( val nombre:String ) : CiudadesIntencion()
    data class Seleccionar(val ciudad: Ciudad) : CiudadesIntencion()
    object ObtenerUbicacion : CiudadesIntencion()
}

