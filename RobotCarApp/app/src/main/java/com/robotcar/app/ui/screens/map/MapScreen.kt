package com.robotcar.app.ui.screens.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.robotcar.app.ui.theme.*

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    // Check location permission
    val hasLocationPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Google Map
        if (hasLocationPermission) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        LatLng(37.7749, -122.4194), // Default to San Francisco
                        15f
                    )
                },
                properties = MapProperties(
                    isMyLocationEnabled = true,
                    mapType = MapType.NORMAL
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {
                // Robot marker
                uiState.robotLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Robot Car",
                        snippet = "Current Location"
                    )
                }
                
                // Destination marker
                uiState.destination?.let { destination ->
                    Marker(
                        state = MarkerState(position = destination),
                        title = "Destination",
                        snippet = "Target Location"
                    )
                }
            }
        } else {
            // Permission denied state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Location permission required for map",
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnBackground
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { /* Request permission */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text("Grant Permission")
                }
            }
        }
        
        // Location Info Card
        Card(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .fillMaxWidth()
                .heightIn(min = 80.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Robot Location",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurface.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(
                        onClick = { viewModel.centerOnRobot() },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Center on Robot",
                            tint = Color.Black
                        )
                    }
                }
                
                uiState.robotLocation?.let { location ->
                    Text(
                        text = "Lat: ${String.format("%.6f", location.latitude)}, Lng: ${String.format("%.6f", location.longitude)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurface
                    )
                } ?: run {
                    Text(
                        text = "Waiting for GPS signal...",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}
