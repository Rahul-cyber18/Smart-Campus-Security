package com.robotcar.app.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.robotcar.app.data.repository.RobotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MapUiState(
    val robotLocation: LatLng? = null,
    val destination: LatLng? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class MapViewModel @Inject constructor(
    private val robotRepository: RobotRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()
    
    init {
        loadRobotLocation()
    }
    
    private fun loadRobotLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Simulate getting robot location from GPS module
            // In real implementation, this would come from the robot's API
            try {
                // Mock location - replace with actual API call
                val mockLocation = LatLng(37.7749, -122.4194)
                
                _uiState.value = _uiState.value.copy(
                    robotLocation = mockLocation,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }
    
    fun setDestination(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                destination = LatLng(latitude, longitude)
            )
        }
    }
    
    fun centerOnRobot() {
        // This would trigger a camera update in the map
        loadRobotLocation()
    }
    
    fun refresh() {
        loadRobotLocation()
    }
}
