package com.robotcar.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        val ROBOT_IP_ADDRESS = stringPreferencesKey("robot_ip_address")
        val ROBOT_PORT = stringPreferencesKey("robot_port")
        val CAMERA_STREAM_URL = stringPreferencesKey("camera_stream_url")
        val WIFI_SSID = stringPreferencesKey("wifi_ssid")
        val BLUETOOTH_DEVICE_ADDRESS = stringPreferencesKey("bluetooth_device_address")
        val LED_STATE = booleanPreferencesKey("led_state")
        val BUZZER_STATE = booleanPreferencesKey("buzzer_state")
        val AUTO_CONNECT = booleanPreferencesKey("auto_connect")
    }
    
    val robotIpAddress: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ROBOT_IP_ADDRESS] ?: "192.168.4.1" // Default ESP32 AP IP
    }
    
    val robotPort: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[ROBOT_PORT] ?: "80"
    }
    
    val cameraStreamUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CAMERA_STREAM_URL] ?: "http://192.168.4.1:81/stream"
    }
    
    val wifiSsid: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[WIFI_SSID] ?: ""
    }
    
    val bluetoothDeviceAddress: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[BLUETOOTH_DEVICE_ADDRESS] ?: ""
    }
    
    val ledState: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[LED_STATE] ?: false
    }
    
    val buzzerState: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[BUZZER_STATE] ?: false
    }
    
    val autoConnect: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[AUTO_CONNECT] ?: false
    }
    
    suspend fun saveRobotIpAddress(ip: String) {
        context.dataStore.edit { preferences ->
            preferences[ROBOT_IP_ADDRESS] = ip
        }
    }
    
    suspend fun saveRobotPort(port: String) {
        context.dataStore.edit { preferences ->
            preferences[ROBOT_PORT] = port
        }
    }
    
    suspend fun saveCameraStreamUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[CAMERA_STREAM_URL] = url
        }
    }
    
    suspend fun saveWifiSsid(ssid: String) {
        context.dataStore.edit { preferences ->
            preferences[WIFI_SSID] = ssid
        }
    }
    
    suspend fun saveBluetoothDeviceAddress(address: String) {
        context.dataStore.edit { preferences ->
            preferences[BLUETOOTH_DEVICE_ADDRESS] = address
        }
    }
    
    suspend fun saveLedState(state: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LED_STATE] = state
        }
    }
    
    suspend fun saveBuzzerState(state: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BUZZER_STATE] = state
        }
    }
    
    suspend fun saveAutoConnect(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_CONNECT] = enabled
        }
    }
}
