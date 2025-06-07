package com.example.aplicacionesmovilesparcial2.presentacion.clima

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaView
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaViewModel
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaViewModelFactory
import com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico.PronosticoView
import com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico.PronosticoViewModel
import com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico.PronosticoViewModelFactory
import com.example.aplicacionesmovilesparcial2.repository.RepositorioApi
import com.example.aplicacionesmovilesparcial2.router.Enrutador
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun ClimaPage(
    navHostController: NavHostController,
    lat : Float,
    lon : Float,
    nombre: String
){
    val viewModel : ClimaViewModel = viewModel(
        factory = ClimaViewModelFactory(
            repositorio = RepositorioApi(),
            router = Enrutador(navHostController),
            lat = lat,
            lon = lon,
            nombre = nombre
        )
    )
    val pronosticoViewModel : PronosticoViewModel = viewModel(
        factory = PronosticoViewModelFactory(
            repositorio = RepositorioApi(),
            router = Enrutador(navHostController),
            lat = lat,
            lon = lon,
            nombre = nombre
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF8EC5FC),
                        Color(0xFFE0C3FC)
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            ClimaView(
                state = viewModel.uiState,
                onAction = { intencion ->
                    viewModel.ejecutar(intencion)
                }
            )
            PronosticoView(
                state = pronosticoViewModel.uiState,
                onAction = { intencion ->
                    pronosticoViewModel.ejecutar(intencion)
                }
            )
        }
    }
}

