package com.example.aplicacionesmovilesparcial2.presentacion.clima.actual

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplicacionesmovilesparcial2.preferencias.Preferencias
import com.example.aplicacionesmovilesparcial2.repository.Repositorio
import com.example.aplicacionesmovilesparcial2.router.Router
import kotlinx.coroutines.launch

class ClimaViewModel(
    val respositorio: Repositorio,
    val router: Router,
    val lat : Float,
    val lon : Float,
    val nombre: String,
    val preferencias: Preferencias
) : ViewModel() {

    var uiState by mutableStateOf<ClimaEstado>(ClimaEstado.Vacio)

    fun ejecutar(intencion: ClimaIntencion){
        when(intencion){
            ClimaIntencion.actualizarClima -> traerClima()
            ClimaIntencion.cambiarCiudad -> cambiarCiudad()

        }
    }

    fun traerClima() {
        uiState = ClimaEstado.Cargando
        viewModelScope.launch {
            try{
                val clima = respositorio.traerClima(lat = lat, lon = lon)
                if(clima != null){
                    uiState = ClimaEstado.Exitoso(
                        ciudad = clima.name ,
                        temperatura = clima.main.temp,
                        descripcion = clima.weather.first().description,
                        st = clima.main.feels_like,
                    )
                } else {
                    uiState = ClimaEstado.Vacio
                }
            } catch (exception: Exception){
                uiState = ClimaEstado.Error(exception.localizedMessage ?: "error desconocido")
            }
        }
    }

    fun cambiarCiudad() {
        preferencias.borrarCiudad()
        router.irACiudades()
    }
}

class ClimaViewModelFactory(
    private val repositorio: Repositorio,
    private val router: Router,
    private val lat: Float,
    private val lon: Float,
    private val nombre: String,
    val preferencias: Preferencias

) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClimaViewModel::class.java)) {
            return ClimaViewModel(repositorio,router,lat,lon,nombre,preferencias) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}