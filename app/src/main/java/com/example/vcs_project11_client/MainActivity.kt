package com.example.vcs_project11_client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.example.vcs_project11_client.ui.screen.HomeScreen
import com.example.vcs_project11_service.ICalculatorService
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    private var calculatorService: ICalculatorService? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val connection =
        object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?
            ) {
                calculatorService =
                    ICalculatorService
                        .Stub
                        .asInterface(service)
            }
            override fun onServiceDisconnected(
                name: ComponentName?
            ) {
                calculatorService = null
            }
        }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        bindCalculatorService()
        setContent {
            HomeScreen(onCalculate = ::calculate
            )
        }
    }
    private fun calculate(
        n1: String,
        n2: String,
        operation: String,
        onResult: (String) -> Unit
    ) {
        scope.launch {
            try {
                val a = n1.toDoubleOrNull()
                val b = n2.toDoubleOrNull()
                if (
                    a == null ||
                    b == null
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Please enter valid numbers",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                if (
                    operation == "/" &&
                    b == 0.0
                ) {
                    Toast.makeText(
                        this@MainActivity,
                        "Cannot divide by zero",
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }
                val result =
                    withContext(
                        Dispatchers.IO
                    ) {
                        when(operation) {
                            "+" ->
                                calculatorService?.add(a, b)
                            "-" ->
                                calculatorService?.subtract(a, b)
                            "*" ->
                                calculatorService?.multiply(a, b)
                            "/" ->
                                calculatorService?.divide(a, b)
                            else -> 0.0
                        }
                    }
                onResult(
                    result.toString()
                )
            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    e.message ?: "Error",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun bindCalculatorService() {
        val intent = Intent("BIND_CALCULATOR_SERVICE")
        intent.setPackage("com.example.vcs_project11_service")
        bindService(
            intent,
            connection,
            BIND_AUTO_CREATE
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
        scope.cancel()
    }
}