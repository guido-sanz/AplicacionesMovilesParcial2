package com.example.aplicacionesmovilesparcial2

import com.example.aplicacionesmovilesparcial2.preferencias.PreferenciasMock
import com.example.aplicacionesmovilesparcial2.presentacion.ciudades.CiudadesEstado
import com.example.aplicacionesmovilesparcial2.presentacion.ciudades.CiudadesIntencion
import com.example.aplicacionesmovilesparcial2.presentacion.ciudades.CiudadesViewModel
import com.example.aplicacionesmovilesparcial2.repository.RepositorioMock
import com.example.aplicacionesmovilesparcial2.repository.RepositorioMockError
import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad
import com.example.aplicacionesmovilesparcial2.repository.modelos.LocationRepositoryMock
import com.example.aplicacionesmovilesparcial2.router.MockRouter
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class CiudadesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testBuscarCiudadesExitoso()= runTest {
        val ciudades = listOf(
            Ciudad(
                name = "Mendoza",
                lat = -23.0f,
                lon = -24.3f,
                country = "Argentina",
                state = "Mendoza"
            ),
            Ciudad(
                name = "Mendoza",
                lat = -23.0f,
                lon = -24.3f,
                country = "Mexico",
                state = "Mendoza"
            )
        )

        val viewModel = CiudadesViewModel(
            router = MockRouter(),
            location = LocationRepositoryMock(),
            repositorio = RepositorioMock(),
            preferencias = PreferenciasMock()
        )

        viewModel.ejecutar(CiudadesIntencion.Buscar("Mendoza"))

        advanceUntilIdle()

        assertEquals(
            CiudadesEstado.Resultado(ciudades),
            viewModel.uiState
        )
    }

    @Test
    fun testBuscarCiudadesVacio() = runTest {
        val viewModel = CiudadesViewModel(
            router = MockRouter(),
            location = LocationRepositoryMock(),
            repositorio = RepositorioMock(),
            preferencias = PreferenciasMock()
        )

        viewModel.ejecutar(CiudadesIntencion.Buscar("Desconocida"))

        assertEquals(CiudadesEstado.Cargando, viewModel.uiState)

        advanceUntilIdle()

        assertEquals(CiudadesEstado.Vacio, viewModel.uiState)
    }

    @Test
    fun testBuscarCiudadesError() = runTest {
        val viewModel = CiudadesViewModel(
            router = MockRouter(),
            location = LocationRepositoryMock(),
            repositorio = RepositorioMockError(),
            preferencias = PreferenciasMock()
        )

        viewModel.ejecutar(CiudadesIntencion.Buscar("Error"))

        assertEquals(CiudadesEstado.Cargando, viewModel.uiState)

        advanceUntilIdle()

        val estado = viewModel.uiState
        assertTrue(estado is CiudadesEstado.Error)
        assertEquals("error desconocido", (estado as CiudadesEstado.Error).mensaje)
    }

    @Test
    fun testSeleccionarClima() {
        val router = MockRouter()
        val viewModel = CiudadesViewModel(
            router = router,
            location = LocationRepositoryMock(),
            repositorio = RepositorioMock(),
            preferencias = PreferenciasMock()
        )

        val ciudad = Ciudad(
            name = "Buenos Aires",
            lat = -34.6f,
            lon = -58.4f,
            country = "Argentina",
            state = "CABA"
        )

        viewModel.ejecutar(CiudadesIntencion.Seleccionar(ciudad))

        assertEquals(
            "clima?lat=${ciudad.lat}&lon=${ciudad.lon}&nombre=${ciudad.name}",
            router.ruta
        )
    }
}