package com.example.aplicacionesmovilesparcial2.repository.modelos

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface LocationRepository {
    suspend fun getLocation() : Location?
    fun getLocation2(onReturn: (Location?) -> Unit)
}

class LocationRepositoryMock : LocationRepository {
    override suspend fun getLocation(): Location? {
        var location = Location(null)
        location.latitude = -34.234
        location.longitude = -58.234
        return location
    }

    override fun getLocation2(onReturn: (Location?) -> Unit) {
        var location = Location(null)
        location.latitude = -34.234
        location.longitude = -58.234
        return onReturn(location)
    }
}

class LocationImplementation(private val context: Context) : LocationRepository {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override suspend fun getLocation(): android.location.Location? {
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

    override fun getLocation2(onReturn: (Location?) -> Unit) {
        TODO("Not yet implemented")
    }
}



