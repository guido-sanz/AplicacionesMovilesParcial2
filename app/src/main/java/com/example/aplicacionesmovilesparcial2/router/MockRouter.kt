package com.example.aplicacionesmovilesparcial2.router

class MockRouter: Router {
    override fun navegar(ruta: Ruta) {
        println("navegar a : ${ ruta.id }" )
    }
}