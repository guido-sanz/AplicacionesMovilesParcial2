package com.example.aplicacionesmovilesparcial2.presentacion.ciudades

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aplicacionesmovilesparcial2.repository.modelos.Ciudad
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices


@Composable
fun CiudadesView(
    modifier: Modifier = Modifier,
    state: CiudadesEstado,
    onAction: (CiudadesIntencion) -> Unit
)

{
    var value by remember { mutableStateOf("") }

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
                modifier = Modifier.weight(1f), // ocupa todo el ancho disponible menos el botón
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            FilledIconButton(
                onClick = { },
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
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        items(ciudades) { ciudad ->
            Card(
                onClick = { onSelect(ciudad) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = ciudad.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    ciudad.country?.let {
                        Text(
                            text = "País: $it",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}