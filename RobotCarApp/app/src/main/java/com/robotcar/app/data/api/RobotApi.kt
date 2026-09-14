package com.robotcar.app.data.api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * API interface for communicating with the ESP32 robot car controller
 */
interface RobotApi {
    
    // Movement Control
    @POST("move/{direction}")
    suspend fun moveRobot(@Path("direction") direction: String): Response<Unit>
    
    @POST("stop")
    suspend fun stopRobot(): Response<Unit>
    
    @POST("speed/{value}")
    suspend fun setSpeed(@Path("value") speed: Int): Response<Unit>
    
    // Camera Control
    @POST("camera/led/{state}")
    suspend fun toggleLed(@Path("state") state: String): Response<Unit>
    
    @POST("camera/buzzer/{state}")
    suspend fun toggleBuzzer(@Path("state") state: String): Response<Unit>
    
    // Status
    @GET("status")
    suspend fun getRobotStatus(): RobotStatusResponse
    
    @GET("battery")
    suspend fun getBatteryLevel(): BatteryResponse
    
    // Connection
    @GET("wifi/scan")
    suspend fun scanWifiNetworks(): List<WifiNetworkResponse>
    
    @POST("wifi/connect")
    suspend fun connectToWifi(ssid: String, password: String): Response<Unit>
    
    @GET("bluetooth/scan")
    suspend fun scanBluetoothDevices(): List<BluetoothDeviceResponse>
    
    @POST("bluetooth/connect/{address}")
    suspend fun connectToBluetooth(@Path("address") address: String): Response<Unit>
    
    // Shutdown
    @POST("shutdown")
    suspend fun shutdownRobot(): Response<Unit>
}

// Response Models
data class RobotStatusResponse(
    val connected: Boolean,
    val moving: String?,
    val speed: Int,
    val ledOn: Boolean,
    val buzzerOn: Boolean
)

data class BatteryResponse(
    val level: Int,
    val voltage: Float,
    val charging: Boolean
)

data class WifiNetworkResponse(
    val ssid: String,
    val signalStrength: Int,
    val secured: Boolean
)

data class BluetoothDeviceResponse(
    val name: String,
    val address: String,
    val bonded: Boolean
)
