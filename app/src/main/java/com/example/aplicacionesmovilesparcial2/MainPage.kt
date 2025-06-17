package com.example.aplicacionesmovilesparcial2

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.aplicacionesmovilesparcial2.presentacion.ciudades.CiudadesPage
import com.example.aplicacionesmovilesparcial2.presentacion.clima.ClimaPage

@Composable
fun MainPage() {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = "ciudades"
    ) {
        composable(
            "ciudades"
        ) {
            CiudadesPage(navHostController)
        }
        composable(
            deepLinks = listOf(navDeepLink { uriPattern="https://climapp.com/ciudad/{nombre}/{lat}/{lon}" }),
            route = "clima?lat={lat}&lon={lon}&nombre={nombre}",
            arguments =  listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType }
            )
        ) {
            val lat = it.arguments?.getFloat("lat") ?: 0.0f
            val lon = it.arguments?.getFloat("lon") ?: 0.0f
            val nombre = it.arguments?.getString("nombre") ?: ""
            ClimaPage(navHostController, lat = lat, lon = lon, nombre = nombre)
        }
    }
}