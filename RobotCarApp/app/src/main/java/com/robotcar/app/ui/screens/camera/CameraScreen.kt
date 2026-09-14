package com.robotcar.app.ui.screens.camera

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.robotcar.app.ui.theme.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SuppressLint("MissingPermission")
@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    
    LaunchedEffect(Unit) {
        viewModel.startCamera(context, cameraExecutor)
    }
    
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopCamera()
            cameraExecutor.shutdown()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Live Camera Feed",
            style = MaterialTheme.typography.titleLarge,
            color = OnBackground,
            modifier = Modifier.padding(16.dp)
        )
        
        // Camera Preview
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkCard),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                },
                update = { previewView ->
                    viewModel.preview?.setSurfaceProvider(previewView.surfaceProvider)
                },
                modifier = Modifier.fillMaxSize()
            )
            
            // Show loading indicator if camera not ready
            if (!viewModel.isCameraReady) {
                CircularProgressIndicator(
                    color = Primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        
        // Control Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LED Toggle
            ControlButton(
                icon = if (viewModel.ledOn) Icons.Default.FlashlightOn else Icons.Default.FlashlightOff,
                label = "LED",
                isActive = viewModel.ledOn,
                onClick = { viewModel.toggleLed() },
                activeColor = ForwardColor
            )
            
            // Buzzer Toggle
            ControlButton(
                icon = if (viewModel.buzzerOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                label = "Buzzer",
                isActive = viewModel.buzzerOn,
                onClick = { viewModel.toggleBuzzer() },
                activeColor = BackwardColor
            )
        }
        
        // Connection Status
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Stream Status:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurface.copy(alpha = 0.7f)
                )
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(RoundedCornerShape(50.percent))
                            .background(if (viewModel.isConnected) ConnectedGreen else DisconnectedRed)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (viewModel.isConnected) "Connected" else "Disconnected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    activeColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier.size(64.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = if (isActive) activeColor else DarkSurface,
                contentColor = if (isActive) Color.Black else OnSurface
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = OnSurface
        )
    }
}
