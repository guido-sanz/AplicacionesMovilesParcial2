package com.example.aplicacionesmovilesparcial2

import com.example.aplicacionesmovilesparcial2.preferencias.PreferenciasMock
import com.example.aplicacionesmovilesparcial2.presentacion.ciudades.CiudadesIntencion
import com.example.aplicacionesmovilesparcial2.presentacion.ciudades.CiudadesViewModel
import com.example.aplicacionesmovilesparcial2.repository.RepositorioMock
import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad
import com.example.aplicacionesmovilesparcial2.repository.modelos.LocationRepositoryMock
import com.example.aplicacionesmovilesparcial2.router.MockRouter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import junit.framework.TestCase.assertEquals
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaIntencion
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain


@OptIn(ExperimentalCoroutinesApi::class)
class PreferenciasIntegracionTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSeleccionarCiudadGuardaEnPreferencias() = runTest {
        val router = MockRouter()
        val preferencias = PreferenciasMock()
        val viewModel = CiudadesViewModel(
            router = router,
            location = LocationRepositoryMock(),
            repositorio = RepositorioMock(),
            preferencias = preferencias
        )

        val ciudad = Ciudad(
            name = "Córdoba",
            lat = -31.4f,
            lon = -64.2f,
            country = "Argentina",
            state = "Córdoba"
        )

        viewModel.ejecutar(CiudadesIntencion.Seleccionar(ciudad))

        // Verificar navegación
        assertEquals(
            "clima?lat=${ciudad.lat}&lon=${ciudad.lon}&nombre=${ciudad.name}",
            router.ruta
        )

        // Verificar que se haya guardado en preferencias
        assertEquals(ciudad.name, preferencias.ciudad?.first)
        assertEquals(ciudad.lat, preferencias.ciudad?.second)
        assertEquals(ciudad.lon, preferencias.ciudad?.third)
    }

    @Test
    fun testCambiarCiudadBorraPreferenciasYNavega() = runTest {
        val router = MockRouter()
        val preferencias = PreferenciasMock()

        // Simulamos que hay una ciudad guardada
        preferencias.guardarCiudad("Córdoba", -31.4f, -64.2f)

        val viewModel = ClimaViewModel(
            respositorio = RepositorioMock(),
            router = router,
            lat = -31.4f,
            lon = -64.2f,
            nombre = "Córdoba",
            preferencias = preferencias
        )

        viewModel.ejecutar(ClimaIntencion.cambiarCiudad)

        val ciudadGuardada = preferencias.obtenerCiudad()
        assertEquals(null, ciudadGuardada)

        assertEquals("ciudades", router.ruta)
    }

}



