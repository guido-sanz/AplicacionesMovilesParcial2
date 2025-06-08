package com.example.aplicacionesmovilesparcial2.presentacion.ciudades

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplicacionesmovilesparcial2.repository.Repositorio
import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad
import com.example.aplicacionesmovilesparcial2.repository.modelos.LocationImplementation
import com.example.aplicacionesmovilesparcial2.router.Router
import com.example.aplicacionesmovilesparcial2.router.Ruta
import kotlinx.coroutines.launch

class CiudadesViewModel(
    val repositorio: Repositorio,
    val router: Router,
    val location: LocationImplementation
) : ViewModel(){

    var uiState by mutableStateOf<CiudadesEstado>(CiudadesEstado.Vacio)
    var ciudades : List<Ciudad> = emptyList()

    fun ejecutar(intencion: CiudadesIntencion){
        when(intencion){
            is CiudadesIntencion.Buscar -> buscar(nombre = intencion.nombre)
            is CiudadesIntencion.Seleccionar -> seleccionar(ciudad = intencion.ciudad)
            is CiudadesIntencion.ObtenerUbicacion -> obtenerUbicacion()
        }
    }

    private fun buscar( nombre: String){

        uiState = CiudadesEstado.Cargando
        viewModelScope.launch {
            try {
                ciudades = repositorio.buscarCiudad(nombre)
                if (ciudades.isEmpty()) {
                    uiState = CiudadesEstado.Vacio
                } else {
                    uiState = CiudadesEstado.Resultado(ciudades)
                }
            } catch (exeption: Exception){
                uiState = CiudadesEstado.Error(exeption.message ?: "error desconocido")
            }
        }
    }

    private fun seleccionar(ciudad: Ciudad){
        val ruta = Ruta.Clima(
            lat = ciudad.lat,
            lon = ciudad.lon,
            nombre = ciudad.name
        )
        router.navegar(ruta)
    }

    private fun obtenerUbicacion() {
        uiState = CiudadesEstado.Cargando
        viewModelScope.launch {
            try {
                val location = location.getLocation()
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    val ciudad = repositorio.buscarCiudadPorLatLon(lat, lon)

                    if (ciudad != null) {
                        seleccionar(ciudad)
                    } else {
                        uiState = CiudadesEstado.Error("No se pudo obtener la ciudad desde la ubicación.")
                    }
                } else {
                    uiState = CiudadesEstado.Error("No se pudo obtener la ubicación.")
                }
            } catch (exception: Exception) {
                uiState = CiudadesEstado.Error(exception.message ?: "Error al obtener ubicación.")
            }
        }
    }
}


class CiudadesViewModelFactory(
    private val repositorio: Repositorio,
    private val router: Router,
    private val location: LocationImplementation
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CiudadesViewModel::class.java)) {
            return CiudadesViewModel(repositorio,router, location) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}