package com.example.aplicacionesmovilesparcial2.presentacion.ciudades

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.aplicacionesmovilesparcial2.repository.RepositorioApi
import com.example.aplicacionesmovilesparcial2.repository.modelos.LocationImplementation
import com.example.aplicacionesmovilesparcial2.router.Enrutador

@Composable
fun CiudadesPage(
    navHostController:  NavHostController
) {
    val context = LocalContext.current
    val viewModel : CiudadesViewModel = viewModel(
        factory = CiudadesViewModelFactory(
            repositorio = RepositorioApi(),
            router = Enrutador(navHostController),
            location = LocationImplementation(context)
        )
    )
    CiudadesView(
        state = viewModel.uiState,
        onAction = { intencion ->
            viewModel.ejecutar(intencion)
        }
    )
}
