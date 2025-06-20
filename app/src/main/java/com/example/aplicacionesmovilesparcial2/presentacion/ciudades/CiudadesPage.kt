package com.example.aplicacionesmovilesparcial2.presentacion.ciudades

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aplicacionesmovilesparcial2.repository.RepositorioApi
import com.example.aplicacionesmovilesparcial2.repository.modelos.LocationImplementation
import com.example.aplicacionesmovilesparcial2.router.RouterImplementation
import com.example.aplicacionesmovilesparcial2.preferencias.PreferenciasUsuario


@Composable
fun CiudadesPage(
    navHostController:  NavHostController
) {
    val context = LocalContext.current
    val preferencias = PreferenciasUsuario(context)

    val viewModel : CiudadesViewModel = viewModel(
        factory = CiudadesViewModelFactory(
            repositorio = RepositorioApi(),
            router = RouterImplementation(navHostController),
            location = LocationImplementation(context),
            preferencias = preferencias
        )
    )
    CiudadesView(
        state = viewModel.uiState,
        onAction = { intencion ->
            viewModel.ejecutar(intencion)
        }
    )
}
