package com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico
import ForecastDay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aplicacionesmovilesparcial2.repository.Repositorio
import com.example.aplicacionesmovilesparcial2.router.Router
import kotlinx.coroutines.launch
import java.time.LocalDate

class PronosticoViewModel(
    val respositorio: Repositorio,
    val router: Router,
    val lat : Float,
    val lon : Float,
    val nombre: String
) : ViewModel() {

    var uiState by mutableStateOf<PronosticoEstado>(PronosticoEstado.Vacio)

    fun ejecutar(intencion: PronosticoIntencion){
        when(intencion){
            PronosticoIntencion.actualizarPronostico -> traerPronostico()
        }
    }

    fun traerPronostico() {
        uiState = PronosticoEstado.Cargando
        viewModelScope.launch {
            try {
                val forecastHours = respositorio.traerPronostico(lat, lon)

                if (forecastHours.isEmpty()) {
                    uiState = PronosticoEstado.Vacio
                } else {
                    val dailyForecast = forecastHours.groupBy {
                        LocalDate.parse(it.dtTxt.substring(0, 10))
                    }.map { (date, forecasts) ->
                        ForecastDay(
                            date = date,
                            tempMax = forecasts.maxOf { it.main.tempMax },
                            tempMin = forecasts.minOf { it.main.tempMin }
                        )
                    }.sortedBy { it.date }

                    uiState = PronosticoEstado.Exitoso(dailyForecast)
                }
            } catch (exception: Exception) {
                uiState = PronosticoEstado.Error(exception.localizedMessage ?: "error desconocido")
            }
        }
    }

}

class PronosticoViewModelFactory(
    private val repositorio: Repositorio,
    private val router: Router,
    private val lat: Float,
    private val lon: Float,
    private val nombre: String,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PronosticoViewModel::class.java)) {
            return PronosticoViewModel(repositorio,router,lat,lon,nombre) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}