package com.robotcar.app.data.repository

import com.robotcar.app.data.api.RobotApi
import com.robotcar.app.data.api.RobotStatusResponse
import com.robotcar.app.data.api.BatteryResponse
import com.robotcar.app.data.api.WifiNetworkResponse
import com.robotcar.app.data.api.BluetoothDeviceResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for robot car operations
 */
@Singleton
class RobotRepository @Inject constructor(
    private val robotApi: RobotApi
) {
    
    // Movement Control
    suspend fun moveForward(): Result<Unit> = try {
        robotApi.moveRobot("forward")
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun moveBackward(): Result<Unit> = try {
        robotApi.moveRobot("backward")
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun moveLeft(): Result<Unit> = try {
        robotApi.moveRobot("left")
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun moveRight(): Result<Unit> = try {
        robotApi.moveRobot("right")
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun stopRobot(): Result<Unit> = try {
        robotApi.stopRobot()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun setSpeed(speed: Int): Result<Unit> = try {
        robotApi.setSpeed(speed)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    // Camera & Accessories Control
    suspend fun toggleLed(state: Boolean): Result<Unit> = try {
        robotApi.toggleLed(if (state) "on" else "off")
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun toggleBuzzer(state: Boolean): Result<Unit> = try {
        robotApi.toggleBuzzer(if (state) "on" else "off")
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    // Status
    suspend fun getRobotStatus(): Result<RobotStatusResponse> = try {
        val response = robotApi.getRobotStatus()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun getBatteryLevel(): Result<BatteryResponse> = try {
        val response = robotApi.getBatteryLevel()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    // WiFi
    suspend fun scanWifiNetworks(): Result<List<WifiNetworkResponse>> = try {
        val networks = robotApi.scanWifiNetworks()
        Result.success(networks)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun connectToWifi(ssid: String, password: String): Result<Unit> = try {
        robotApi.connectToWifi(ssid, password)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    // Bluetooth
    suspend fun scanBluetoothDevices(): Result<List<BluetoothDeviceResponse>> = try {
        val devices = robotApi.scanBluetoothDevices()
        Result.success(devices)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    suspend fun connectToBluetooth(address: String): Result<Unit> = try {
        robotApi.connectToBluetooth(address)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
    
    // Shutdown
    suspend fun shutdownRobot(): Result<Unit> = try {
        robotApi.shutdownRobot()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
