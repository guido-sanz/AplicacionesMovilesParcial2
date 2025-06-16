package com.example.aplicacionesmovilesparcial2

import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaEstado
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaIntencion
import com.example.aplicacionesmovilesparcial2.presentacion.clima.actual.ClimaViewModel
import com.example.aplicacionesmovilesparcial2.repository.RepositorioMock
import com.example.aplicacionesmovilesparcial2.repository.RepositorioMockError
import com.example.aplicacionesmovilesparcial2.router.MockRouter
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClimaViewModelTest{

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
    fun actualizarClimaEstadoExistoso() = runTest {

        val viewModel = ClimaViewModel(
            respositorio = RepositorioMock(),
            router = MockRouter(),
            lat = -34.6f,
            lon = -58.4f,
            nombre = "Buenos Aires"
        )

        viewModel.ejecutar(ClimaIntencion.actualizarClima)

        assertEquals(ClimaEstado.Cargando, viewModel.uiState)

        advanceUntilIdle()

        val estado = viewModel.uiState
        assertTrue(estado is ClimaEstado.Exitoso)
    }

    @Test
    fun actualizarClimaEstadoError() = runTest {

        val viewModel = ClimaViewModel(
            respositorio = RepositorioMockError(),
            router = MockRouter(),
            lat = -34.0f,
            lon = -58.0f,
            nombre = "Buenos Aires"
        )

        viewModel.ejecutar(ClimaIntencion.actualizarClima)

        assertEquals(ClimaEstado.Cargando, viewModel.uiState)

        advanceUntilIdle()

        val estado = viewModel.uiState
        assertTrue(estado is ClimaEstado.Error)
        assertEquals("error desconocido", (estado as ClimaEstado.Error).mensaje)
    }

    @Test
    fun actualizarClimaEstadoVacio() = runTest {

        val viewModel = ClimaViewModel(
            respositorio = RepositorioMock(),
            router = MockRouter(),
            lat = -34.6f,
            lon = -58.0f,
            nombre = "Buenos Aires"
        )

        viewModel.ejecutar(ClimaIntencion.actualizarClima)

        advanceUntilIdle()

        assertEquals(ClimaEstado.Vacio, viewModel.uiState)
    }
}