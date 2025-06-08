package com.example.aplicacionesmovilesparcial2.presentacion.ciudades

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad


@Composable
fun CiudadesView(
    modifier: Modifier = Modifier,
    state: CiudadesEstado,
    onAction: (CiudadesIntencion) -> Unit
)

{
    var value by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
    ) {
        AnimatedGradientBackground()
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Seleccioná una ciudad",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = {
                        value = it
                        onAction(CiudadesIntencion.Buscar(it))
                    },
                    label = { Text("Buscar por nombre") },
                    placeholder = { Text("Ej: Buenos Aires") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar"
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                FilledIconButton(
                    onClick = { onAction(CiudadesIntencion.ObtenerUbicacion) },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Usar mi ubicación"
                    )
                }
            }

            when (state) {
                CiudadesEstado.Cargando -> {
                    LoadingCiudadesView()
                }
                is CiudadesEstado.Error -> {
                    ErrorCiudadesView(mensaje = state.mensaje)
                }
                is CiudadesEstado.Resultado -> {
                    if (state.ciudades.isEmpty()) {
                        SinResultadosView()
                    } else {
                        ListaDeCiudades(
                            ciudades = state.ciudades,
                            onSelect = { onAction(CiudadesIntencion.Seleccionar(it)) }
                        )
                    }
                }
                CiudadesEstado.Vacio -> {
                    EstadoInicialView()
                }
            }

        }
    }
}

@Composable
fun LoadingCiudadesView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(12.dp))
            Text("Buscando ciudades...", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun ErrorCiudadesView(mensaje: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = mensaje,
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun SinResultadosView() {
    Text(
        text = "No se encontraron resultados.",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = 32.dp)
    )
}

@Composable
fun EstadoInicialView() {
    Text(
        text = "Comenzá escribiendo para buscar una ciudad.",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(top = 32.dp)
    )
}

@Composable
fun ListaDeCiudades(
    ciudades: List<Ciudad>,
    onSelect: (Ciudad) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        items(ciudades) { ciudad ->
            Card(
                onClick = { onSelect(ciudad) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp), // Bordes más redondeados
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth()
                ) {
                    // Nombre de ciudad
                    Text(
                        text = ciudad.name,
                        style = MaterialTheme.typography.titleLarge, // Más grande
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Provincia / Estado
                    ciudad.state?.let {
                        Text(
                            text = "Provincia/Estado: $it",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    // País con bandera
                    ciudad.country?.let {
                        Text(
                            text = "País: $it",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Coordenadas con íconos
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Latitud",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${String.format("%.2f", ciudad.lat)}°",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "${String.format("%.2f", ciudad.lon)}°",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedGradientBackground() {
    val transition = rememberInfiniteTransition()

    // Definimos dos colores que van a animarse
    val color1 by transition.animateColor(
        initialValue = Color(0xFF89F7FE),  // Celeste claro
        targetValue = Color(0xFF66A6FF),   // Azul más fuerte
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val color2 by transition.animateColor(
        initialValue = Color(0xFFFBC2EB),  // Rosa suave
        targetValue = Color(0xFFA6C1EE),   // Azul lavanda
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(color1, color2)
                )
            )
    )
}

