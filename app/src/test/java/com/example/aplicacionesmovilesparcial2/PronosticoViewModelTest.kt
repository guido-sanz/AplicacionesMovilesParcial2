package com.example.aplicacionesmovilesparcial2

import com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico.PronosticoEstado
import com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico.PronosticoIntencion
import com.example.aplicacionesmovilesparcial2.presentacion.clima.pronostico.PronosticoViewModel
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
class PronosticoViewModelTest {

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
    fun actualizarPronosticoEstadoExistoso() = runTest {

        val viewModel = PronosticoViewModel(
            respositorio = RepositorioMock(),
            router = MockRouter(),
            lat = -34.6f,
            lon = -58.4f,
            nombre = "Buenos Aires"
        )

        viewModel.ejecutar(PronosticoIntencion.actualizarPronostico)

        assertEquals(PronosticoEstado.Cargando, viewModel.uiState)

        advanceUntilIdle()

        val estado = viewModel.uiState
        assertTrue(estado is PronosticoEstado.Exitoso)
    }

    @Test
    fun actualizarPronosticoEstadoError() = runTest {

        val viewModel = PronosticoViewModel(
            respositorio = RepositorioMockError(),
            router = MockRouter(),
            lat = -34.0f,
            lon = -58.0f,
            nombre = "Buenos Aires"
        )

        viewModel.ejecutar(PronosticoIntencion.actualizarPronostico)

        assertEquals(PronosticoEstado.Cargando, viewModel.uiState)

        advanceUntilIdle()

        val estado = viewModel.uiState
        assertTrue(estado is PronosticoEstado.Error)
        assertEquals("error desconocido", (estado as PronosticoEstado.Error).mensaje)
    }

    @Test
    fun actualizarPronosticoEstadoVacio() = runTest {

        val viewModel = PronosticoViewModel(
            respositorio = RepositorioMock(),
            router = MockRouter(),
            lat = -34.6f,
            lon = -58.0f,
            nombre = "Buenos Aires"
        )

        viewModel.ejecutar(PronosticoIntencion.actualizarPronostico)

        advanceUntilIdle()

        assertEquals(PronosticoEstado.Vacio, viewModel.uiState)
    }

}