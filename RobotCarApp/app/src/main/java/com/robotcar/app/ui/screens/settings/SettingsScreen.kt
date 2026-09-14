package com.robotcar.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robotcar.app.ui.navigation.Screen
import com.robotcar.app.ui.theme.*

@Composable
fun SettingsScreen(
    onNavigateToWifi: () -> Unit,
    onNavigateToBluetooth: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleLarge,
            color = OnBackground,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Connection Settings Section
        SettingsSection(title = "Connection") {
            SettingsItem(
                icon = Icons.Default.Wifi,
                title = "WiFi Settings",
                subtitle = "Connect to WiFi network",
                onClick = onNavigateToWifi
            )
            
            HorizontalDivider(color = DarkSurface, modifier = Modifier.padding(vertical = 8.dp))
            
            SettingsItem(
                icon = Icons.Default.Bluetooth,
                title = "Bluetooth Settings",
                subtitle = "Pair with Bluetooth device",
                onClick = onNavigateToBluetooth
            )
            
            HorizontalDivider(color = DarkSurface, modifier = Modifier.padding(vertical = 8.dp))
            
            // Robot IP Configuration
            SettingsItem(
                icon = Icons.Default.Dns,
                title = "Robot IP Address",
                subtitle = uiState.robotIp,
                onClick = { /* Show IP configuration dialog */ }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Robot Control Section
        SettingsSection(title = "Robot Control") {
            SettingsItem(
                icon = Icons.Default.FlashlightOn,
                title = "LED Control",
                subtitle = if (uiState.ledOn) "LED is ON" else "LED is OFF",
                trailingContent = {
                    Switch(
                        checked = uiState.ledOn,
                        onCheckedChange = { viewModel.toggleLed(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = ForwardColor
                        )
                    )
                }
            )
            
            HorizontalDivider(color = DarkSurface, modifier = Modifier.padding(vertical = 8.dp))
            
            SettingsItem(
                icon = Icons.Default.VolumeUp,
                title = "Buzzer Control",
                subtitle = if (uiState.buzzerOn) "Buzzer is ON" else "Buzzer is OFF",
                trailingContent = {
                    Switch(
                        checked = uiState.buzzerOn,
                        onCheckedChange = { viewModel.toggleBuzzer(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = BackwardColor
                        )
                    )
                }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // System Section
        SettingsSection(title = "System") {
            SettingsItem(
                icon = Icons.Default.PowerSettingsNew,
                title = "Shut Down Robot",
                subtitle = "Power off the robot car",
                onClick = { viewModel.shutdownRobot() },
                textColor = Error
            )
            
            HorizontalDivider(color = DarkSurface, modifier = Modifier.padding(vertical = 8.dp))
            
            SettingsItem(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "Version 1.0.0",
                onClick = { /* Show about dialog */ }
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Auto-connect Toggle
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Auto-Connect",
                            style = MaterialTheme.typography.bodyLarge,
                            color = OnSurface,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Connect automatically on startup",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                
                Switch(
                    checked = uiState.autoConnect,
                    onCheckedChange = { viewModel.setAutoConnect(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = Primary
                    )
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        content()
    }
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    textColor: Color = OnSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurface.copy(alpha = 0.6f)
            )
        }
        if (trailingContent != {}) {
            trailingContent()
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = OnSurface.copy(alpha = 0.5f)
            )
        }
    }
}
