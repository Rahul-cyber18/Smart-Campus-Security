package com.robotcar.app.ui.screens.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robotcar.app.data.repository.RobotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val robotRepository: RobotRepository
) : ViewModel() {
    
    private val _ledOn = MutableStateFlow(false)
    val ledOn: StateFlow<Boolean> = _ledOn.asStateFlow()
    
    private val _buzzerOn = MutableStateFlow(false)
    val buzzerOn: StateFlow<Boolean> = _buzzerOn.asStateFlow()
    
    private val _isCameraReady = MutableStateFlow(false)
    val isCameraReady: StateFlow<Boolean> = _isCameraReady.asStateFlow()
    
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()
    
    var preview: Preview? = null
        private set
    
    private var cameraProvider: ProcessCameraProvider? = null
    
    fun startCamera(context: Context, cameraExecutor: ExecutorService) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        
        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(ContextCompat.getMainExecutor(context)) { surfaceProvider ->
                        _isCameraReady.value = true
                    }
                }
                
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                
                try {
                    cameraProvider?.unbindAll()
                    cameraProvider?.bindToLifecycle(
                        this,
                        cameraSelector,
                        preview
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
        
        checkConnection()
    }
    
    fun stopCamera() {
        cameraProvider?.unbindAll()
        preview = null
    }
    
    fun toggleLed() {
        viewModelScope.launch {
            val newState = !_ledOn.value
            robotRepository.toggleLed(newState).onSuccess {
                _ledOn.value = newState
            }
        }
    }
    
    fun toggleBuzzer() {
        viewModelScope.launch {
            val newState = !_buzzerOn.value
            robotRepository.toggleBuzzer(newState).onSuccess {
                _buzzerOn.value = newState
            }
        }
    }
    
    private fun checkConnection() {
        viewModelScope.launch {
            robotRepository.getRobotStatus().onSuccess { status ->
                _isConnected.value = status.connected
                _ledOn.value = status.ledOn
                _buzzerOn.value = status.buzzerOn
            }.onFailure {
                _isConnected.value = false
            }
        }
    }
}
