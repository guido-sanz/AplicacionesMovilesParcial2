package com.example.aplicacionesmovilesparcial2.presentacion.clima.actual

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.example.aplicacionesmovilesparcial2.ui.theme.AplicacionesMovilesParcial2Theme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import kotlin.math.roundToInt


@Composable
fun ClimaView(
    modifier: Modifier = Modifier,
    state: ClimaEstado,
    onAction: (ClimaIntencion) -> Unit,
    mensajeCompartir: String,
    context: Context
) {
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        onAction(ClimaIntencion.actualizarClima)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state) {
            is ClimaEstado.Error -> ErrorView(mensaje = state.mensaje)
            is ClimaEstado.Exitoso -> {
                OutlinedButton(
                    onClick = { onAction(ClimaIntencion.cambiarCiudad) },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp, end = 8.dp),
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Cambiar ciudad",
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text("Cambiar ciudad")
                }

                ClimaDetalle(
                    ciudad = state.ciudad,
                    temperatura = state.temperatura,
                    descripcion = state.descripcion,
                    st = state.st,
                    mensajeCompartir = mensajeCompartir,
                    context = context
                )
            }
            ClimaEstado.Vacio -> LoadingView()
            ClimaEstado.Cargando -> EmptyView()
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ClimaDetalle(
    ciudad: String,
    temperatura: Double,
    descripcion: String,
    st: Double,
    mensajeCompartir: String,
    context: Context
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            IconButton(
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, mensajeCompartir)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Compartir",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 16.dp) // Evita que se superponga con el botón
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = ciudad,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${temperatura.roundToInt()}°C",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = descripcion.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sensación térmica: ${st.roundToInt()}°C",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun EmptyView() {
    Text(
        text = "No hay datos para mostrar",
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center
    )
}

@Composable
fun LoadingView() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text("Cargando clima...", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ErrorView(mensaje: String) {
    Text(
        text = mensaje,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun ClimaPreviewVacio() {
    AplicacionesMovilesParcial2Theme {
        ClimaView(
            state = ClimaEstado.Vacio,
            onAction = {},
            mensajeCompartir = "",
            context = LocalContext.current
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClimaPreviewError() {
    AplicacionesMovilesParcial2Theme {
        ClimaView(
            state = ClimaEstado.Error("¡Ups! Ocurrió un error al obtener el clima."),
            onAction = {},
            mensajeCompartir = "",
            context = LocalContext.current
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClimaPreviewExitoso() {
    AplicacionesMovilesParcial2Theme {
        ClimaView(
            state = ClimaEstado.Exitoso(
                ciudad = "Mendoza",
                temperatura = 22.5,
                descripcion = "parcialmente nublado",
                st = 24.0
            ),
            onAction = {},
            mensajeCompartir =  "",
            context = LocalContext.current
        )
    }
}