package com.example.aplicacionesmovilesparcial2.repository.modelos

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface LocationRepository {
    suspend fun getLocation() : Location?
}

class LocationImplementation(private val context: Context) : LocationRepository {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getLocation(): Location? {
        return suspendCancellableCoroutine { continuation ->
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    continuation.resume(location)
                }
            } catch (e: SecurityException) {
                continuation.resume(null)
            }
        }
    }
}



