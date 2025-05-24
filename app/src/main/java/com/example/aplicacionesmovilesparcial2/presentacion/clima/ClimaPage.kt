package com.example.aplicacionesmovilesparcial2.presentacion.clima

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
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
            nombre = nombre
        )
    )

    Column {
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
