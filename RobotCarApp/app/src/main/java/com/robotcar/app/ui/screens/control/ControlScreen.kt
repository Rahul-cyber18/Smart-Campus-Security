package com.robotcar.app.ui.screens.control

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robotcar.app.ui.theme.*

@Composable
fun ControlScreen(
    viewModel: ControlViewModel = hiltViewModel()
) {
    val haptic = LocalHapticFeedback.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Manual Control",
            style = MaterialTheme.typography.titleLarge,
            color = OnBackground,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Directional Control Pad
        Box(
            modifier = Modifier
                .size(280.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Forward Button
            DirectionButton(
                icon = Icons.Default.ArrowUpward,
                color = ForwardColor,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.moveForward()
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-60).dp)
            )
            
            // Backward Button
            DirectionButton(
                icon = Icons.Default.ArrowDownward,
                color = BackwardColor,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.moveBackward()
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 60.dp)
            )
            
            // Left Button
            DirectionButton(
                icon = Icons.Default.ArrowBack,
                color = LeftColor,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.moveLeft()
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(x = (-60).dp)
            )
            
            // Right Button
            DirectionButton(
                icon = Icons.Default.ArrowForward,
                color = RightColor,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.moveRight()
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 60.dp)
            )
            
            // Stop Button (Center)
            DirectionButton(
                icon = Icons.Default.Stop,
                color = StopColor,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    viewModel.stopRobot()
                },
                modifier = Modifier.size(80.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Speed Control Slider
        SpeedControlSlider(
            onSpeedChanged = { speed ->
                viewModel.setSpeed(speed)
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // LED and Buzzer Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ControlToggle(
                label = "LED Light",
                isOn = viewModel.ledOn,
                onToggle = { viewModel.toggleLed(it) },
                onColor = ForwardColor
            )
            
            ControlToggle(
                label = "Buzzer",
                isOn = viewModel.buzzerOn,
                onToggle = { viewModel.toggleBuzzer(it) },
                onColor = BackwardColor
            )
        }
    }
}

@Composable
private fun DirectionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier.size(70.dp),
        shape = CircleShape,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = color,
            contentColor = Color.Black
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
private fun SpeedControlSlider(
    onSpeedChanged: (Int) -> Unit
) {
    var sliderPosition by remember { mutableStateOf(50f) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Speed: ${sliderPosition.toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurface,
                fontWeight = FontWeight.Bold
            )
            
            Slider(
                value = sliderPosition,
                onValueChange = { 
                    sliderPosition = it
                    onSpeedChanged(it.toInt())
                },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = Primary,
                    activeTrackColor = Primary,
                    inactiveTrackColor = Primary.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
private fun ControlToggle(
    label: String,
    isOn: Boolean,
    onToggle: (Boolean) -> Unit,
    onColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Switch(
            checked = isOn,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = onColor,
                uncheckedThumbColor = OnSurface.copy(alpha = 0.5f),
                uncheckedTrackColor = DarkSurface
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = OnSurface
        )
    }
}
