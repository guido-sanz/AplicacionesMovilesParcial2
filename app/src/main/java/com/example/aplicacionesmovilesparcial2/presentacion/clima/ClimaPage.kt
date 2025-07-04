package com.example.aplicacionesmovilesparcial2.presentacion.clima

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaView
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaViewModel
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaViewModelFactory
import com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico.PronosticoView
import com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico.PronosticoViewModel
import com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico.PronosticoViewModelFactory
import com.example.aplicacionesmovilesparcial2.repository.RepositorioApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aplicacionesmovilesparcial2.router.RouterImplementation
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import com.example.aplicacionesmovilesparcial2.preferencias.PreferenciasUsuario


@Composable
fun ClimaPage(
    navHostController: NavHostController,
    lat : Float,
    lon : Float,
    nombre: String
){
    val context = LocalContext.current
    val preferencias = PreferenciasUsuario(context)

    val urlCompartir = remember(nombre, lat, lon) {
        val nombreCodificado = URLEncoder.encode(nombre, StandardCharsets.UTF_8.toString())
        "https://climapp.com/ciudad/$nombreCodificado/$lat/$lon"
    }

    val mensajeCompartir = remember(urlCompartir) {
        "Mira el clima de $nombre en Climapp! $urlCompartir"
    }

    val viewModel : ClimaViewModel = viewModel(
        factory = ClimaViewModelFactory(
            repositorio = RepositorioApi(),
            router = RouterImplementation(navHostController),
            lat = lat,
            lon = lon,
            nombre = nombre,
            preferencias = preferencias
        )
    )

    val pronosticoViewModel : PronosticoViewModel = viewModel(
        factory = PronosticoViewModelFactory(
            repositorio = RepositorioApi(),
            router = RouterImplementation(navHostController),
            lat = lat,
            lon = lon,
            nombre = nombre
        )
    )

    val climaState = viewModel.uiState

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ClimaBackground(climaState)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {

            ClimaView(
                state = viewModel.uiState,
                onAction = { intencion -> viewModel.ejecutar(intencion) },
                mensajeCompartir = mensajeCompartir,
                context = context
            )

            PronosticoView(
                state = pronosticoViewModel.uiState,
                onAction = { intencion -> pronosticoViewModel.ejecutar(intencion) }
            )
        }
    }
}
