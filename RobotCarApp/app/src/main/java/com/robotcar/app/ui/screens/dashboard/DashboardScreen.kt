package com.robotcar.app.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.SignalWifi4Bar
import androidx.compose.material.icons.filled.Robot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robotcar.app.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Robot Car Dashboard",
            style = MaterialTheme.typography.titleLarge,
            color = OnBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Connection Status Card
        StatusCard(
            title = "Connection Status",
            value = if (uiState.isConnected) "Connected" else "Disconnected",
            valueColor = if (uiState.isConnected) ConnectedGreen else DisconnectedRed,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Battery Level Card
        StatusCard(
            title = "Battery Level",
            value = "${uiState.batteryLevel}%",
            valueColor = if (uiState.batteryLevel > 20) ForwardColor else Error,
            icon = Icons.Default.BatteryFull,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Signal Strength Card
        StatusCard(
            title = "Signal Strength",
            value = "${uiState.signalStrength}%",
            valueColor = Primary,
            icon = Icons.Default.SignalWifi4Bar,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Robot Status Card
        StatusCard(
            title = "Robot Status",
            value = uiState.robotStatus,
            valueColor = Secondary,
            icon = Icons.Default.Robot,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // LED and Buzzer Status
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ToggleIndicator(
                label = "LED",
                isOn = uiState.ledOn,
                onColor = ForwardColor
            )
            
            ToggleIndicator(
                label = "Buzzer",
                isOn = uiState.buzzerOn,
                onColor = BackwardColor
            )
        }
    }
}

@Composable
private fun StatusCard(
    title: String,
    value: String,
    valueColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = valueColor,
                    fontWeight = FontWeight.Bold
                )
            }
            
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Primary,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
private fun ToggleIndicator(
    label: String,
    isOn: Boolean,
    onColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(50.percent))
                .background(if (isOn) onColor else DarkSurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isOn) Icons.Default.BatteryFull else Icons.Default.BatteryFull,
                contentDescription = label,
                tint = if (isOn) Color.Black else OnSurface.copy(alpha = 0.3f),
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurface
        )
    }
}
