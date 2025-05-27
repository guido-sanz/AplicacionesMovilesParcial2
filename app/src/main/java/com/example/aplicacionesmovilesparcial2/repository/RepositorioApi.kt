package com.example.aplicacionesmovilesparcial2.repository

import ForecastHour
import WeatherResponse
import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad
import com.example.aplicacionesmovilesparcial2.repository.modelos.Clima
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class RepositorioApi : Repositorio {

    private val apiKey = "95e93e4f7a36fc511148468d1774792d"

    private val cliente = HttpClient(){
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    override suspend fun buscarCiudad(ciudad: String): List<Ciudad> {
        val respuesta = cliente.get("https://api.openweathermap.org/geo/1.0/direct"){
            parameter("q",ciudad)
            parameter("limit",100)
            parameter("appid",apiKey)
        }

        if (respuesta.status == HttpStatusCode.OK){
            val ciudades = respuesta.body<List<Ciudad>>()
            return ciudades
        }else{
            throw Exception()
        }
    }

    override suspend fun traerClima(lat: Float, lon: Float): Clima {
        val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/weather"){
            parameter("lat",lat)
            parameter("lon",lon)
            parameter("units","metric")
            parameter("appid",apiKey)
        }
        if (respuesta.status == HttpStatusCode.OK){
            val clima = respuesta.body<Clima>()
            return clima
        }else{
            throw Exception()
        }
    }

    override suspend fun traerPronostico(lat: Float, lon: Float): List<ForecastHour> {
        return try {
            val respuesta = cliente.get("https://api.openweathermap.org/data/2.5/forecast") {
                parameter("lat", lat)
                parameter("lon", lon)
                parameter("cnt", 4)
                parameter("units", "metric")
                parameter("appid", apiKey)
            }

            if (respuesta.status == HttpStatusCode.OK) {
                val forecast = respuesta.body<WeatherResponse>()
                forecast.list
            } else {
                throw Exception("Respuesta no exitosa: ${respuesta.status}")
            }
        } catch (e: Exception) {
            println("Error al traer el pronóstico: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}