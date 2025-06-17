package com.example.aplicacionesmovilesparcial2.repository

import ForecastHour
import MainData
import Weather
import Clouds
import Wind
import Sys
import Rain
import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad
import com.example.aplicacionesmovilesparcial2.repository.modelos.Clima
import com.example.aplicacionesmovilesparcial2.repository.modelos.Coord
import com.example.aplicacionesmovilesparcial2.repository.modelos.Main

class RepositorioMock  : Repositorio {

    val cordoba = Ciudad(
        name = "Cordoba",
        lat = -23.0f,
        lon = -24.3f,
        country = "Argentina",
        state = "Cordoba"
    )
    val bsAs = Ciudad(
        name = "Buenos Aires",
        lat = -23.0f,
        lon = -24.3f,
        country = "Argentina",
        state = "Buenos Aires"
    )
    val mendoza = Ciudad(
        name = "Mendoza",
        lat = -23.0f,
        lon = -24.3f,
        country = "Argentina",
        state = "Mendoza"
    )
    val mendozaMexico = Ciudad(
        name = "Mendoza",
        lat = -23.0f,
        lon = -24.3f,
        country = "Mexico",
        state = "Mendoza"
    )

    val ciudades = listOf(cordoba,bsAs,mendoza,mendozaMexico)

    override suspend fun buscarCiudad(ciudad: String): List<Ciudad> {
        if (ciudad == "error"){
            throw Exception()
        }
        return ciudades.filter { it.name.contains(ciudad,ignoreCase = true) }
    }

    override suspend fun traerClima(lat: Float, lon: Float): Clima? {
        return if (lat == -34.6f && lon == -58.4f) {
            Clima(
                base = "stations",
                name = "Buenos Aires",
                coord = Coord(lon = -58.4, lat = -34.6),
                weather = listOf(
                    com.example.aplicacionesmovilesparcial2.repository.modelos.Weather(
                        id = 800,
                        main = "Clear",
                        description = "cielo despejado",
                        icon = "01d"
                    )
                ),
                main = Main(
                    temp = 23.5,
                    feels_like = 22.0,
                    temp_min = 20.0,
                    temp_max = 26.0,
                    pressure = 1013,
                    humidity = 60
                ),
                wind = com.example.aplicacionesmovilesparcial2.repository.modelos.Wind(
                    speed = 3.4,
                    deg = 180,
                    gust = 5.0
                ),
                clouds = com.example.aplicacionesmovilesparcial2.repository.modelos.Clouds(all = 10)
            )
        } else {
            return null;
        }
    }

    override suspend fun traerPronostico(lat: Float, lon: Float): List<ForecastHour> {
        return if (lat == -34.6f && lon == -58.4f) {
            listOf(
                ForecastHour(
                    dt = 1718707200L,
                    dtTxt = "2025-06-18 12:00:00",
                    main = MainData(
                        temp = 15.5,
                        feelsLike = 15.0,
                        tempMin = 12.0,
                        tempMax = 18.0,
                        pressure = 1013,
                        seaLevel = 1013,
                        groundLevel = 1009,
                        humidity = 70,
                        tempKf = 0.5
                    ),
                    weather = listOf(
                        Weather(
                            id = 800,
                            main = "Clear",
                            description = "cielo despejado",
                            icon = "01d"
                        )
                    ),
                    clouds = Clouds(all = 0),
                    wind = Wind(speed = 3.5, deg = 180, gust = 5.0),
                    visibility = 10000,
                    pop = 0.0,
                    rain = null,
                    sys = Sys(pod = "d")
                ),
                ForecastHour(
                    dt = 1718718000L,
                    dtTxt = "2025-06-18 18:00:00",
                    main = MainData(
                        temp = 13.0,
                        feelsLike = 12.0,
                        tempMin = 10.0,
                        tempMax = 14.0,
                        pressure = 1012,
                        seaLevel = 1012,
                        groundLevel = 1008,
                        humidity = 75,
                        tempKf = 0.4
                    ),
                    weather = listOf(
                        Weather(
                            id = 500,
                            main = "Rain",
                            description = "lluvia ligera",
                            icon = "10d"
                        )
                    ),
                    clouds = Clouds(all = 80),
                    wind = Wind(speed = 2.0, deg = 190, gust = 4.0),
                    visibility = 9000,
                    pop = 0.5,
                    rain = Rain(volume = 1.2),
                    sys = Sys(pod = "d")
                )
            )
        } else {
            emptyList()
        }
    }

    override suspend fun buscarCiudadPorLatLon(lat: Double, lon: Double): Ciudad? {
        if (lat == -34.234 && lon == -58.234) {
            val ciudadBuscada = ciudades.find { it.lat.toDouble() == lat && it.lon.toDouble() == lon }
            return ciudadBuscada
        } else {
            return null
        }
    }

}


class RepositorioMockError  : Repositorio {

    override suspend fun buscarCiudad(ciudad: String): List<Ciudad> {
        throw Exception()
    }

    override suspend fun traerClima(lat: Float, lon: Float): Clima {
        throw Exception()
    }

    override suspend fun traerPronostico(lat: Float, lon: Float): List<ForecastHour> {
        throw Exception()
    }

    override suspend fun buscarCiudadPorLatLon(lat: Double, lon: Double): Ciudad? {
        throw Exception()
    }
}