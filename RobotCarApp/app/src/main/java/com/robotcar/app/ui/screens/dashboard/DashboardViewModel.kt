package com.robotcar.app.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotcar.app.data.repository.RobotRepository
import com.robotcar.app.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isConnected: Boolean = false,
    val batteryLevel: Int = 0,
    val signalStrength: Int = 0,
    val robotStatus: String = "Idle",
    val ledOn: Boolean = false,
    val buzzerOn: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val robotRepository: RobotRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    init {
        loadDashboardData()
    }
    
    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Simulate loading data - in real app, combine flows from repositories
            try {
                // Get robot status
                robotRepository.getRobotStatus().onSuccess { status ->
                    _uiState.value = _uiState.value.copy(
                        isConnected = status.connected,
                        robotStatus = status.moving ?: "Idle",
                        ledOn = status.ledOn,
                        buzzerOn = status.buzzerOn
                    )
                }
                
                // Get battery level
                robotRepository.getBatteryLevel().onSuccess { battery ->
                    _uiState.value = _uiState.value.copy(
                        batteryLevel = battery.level
                    )
                }
                
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    fun refresh() {
        loadDashboardData()
    }
}
