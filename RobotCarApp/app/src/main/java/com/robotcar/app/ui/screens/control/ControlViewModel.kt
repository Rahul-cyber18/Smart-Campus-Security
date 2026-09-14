package com.robotcar.app.ui.screens.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotcar.app.data.repository.RobotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ControlViewModel @Inject constructor(
    private val robotRepository: RobotRepository
) : ViewModel() {
    
    private val _ledOn = MutableStateFlow(false)
    val ledOn: StateFlow<Boolean> = _ledOn.asStateFlow()
    
    private val _buzzerOn = MutableStateFlow(false)
    val buzzerOn: StateFlow<Boolean> = _buzzerOn.asStateFlow()
    
    fun moveForward() {
        viewModelScope.launch {
            robotRepository.moveForward()
        }
    }
    
    fun moveBackward() {
        viewModelScope.launch {
            robotRepository.moveBackward()
        }
    }
    
    fun moveLeft() {
        viewModelScope.launch {
            robotRepository.moveLeft()
        }
    }
    
    fun moveRight() {
        viewModelScope.launch {
            robotRepository.moveRight()
        }
    }
    
    fun stopRobot() {
        viewModelScope.launch {
            robotRepository.stopRobot()
        }
    }
    
    fun setSpeed(speed: Int) {
        viewModelScope.launch {
            robotRepository.setSpeed(speed)
        }
    }
    
    fun toggleLed(state: Boolean) {
        viewModelScope.launch {
            robotRepository.toggleLed(state).onSuccess {
                _ledOn.value = state
            }
        }
    }
    
    fun toggleBuzzer(state: Boolean) {
        viewModelScope.launch {
            robotRepository.toggleBuzzer(state).onSuccess {
                _buzzerOn.value = state
            }
        }
    }
}
