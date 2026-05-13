package com.example.vcs_project11_client

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vcs_project11_service.ICalculatorService
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    private var calculatorService:
            ICalculatorService? = null

    private val scope =
        CoroutineScope(
            Dispatchers.Main +
                    SupervisorJob()
        )

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

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        bindCalculatorService()

        setContent {

            var number1 by remember {
                mutableStateOf("")
            }

            var number2 by remember {
                mutableStateOf("")
            }

            var result by remember {
                mutableStateOf("")
            }

            val gradient =
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E3A8A)
                    )
                )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradient)
                    .padding(20.dp)
            ) {

                Column {

                    Text(
                        text = "Calculator Client",
                        color = Color.White,
                        fontSize = 28.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(20.dp)
                    )

                    OutlinedTextField(
                        value = number1,
                        onValueChange = {
                            number1 = it
                        },
                        label = {
                            Text("Number 1")
                        },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White
                        )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    OutlinedTextField(
                        value = number2,
                        onValueChange = {
                            number2 = it
                        },
                        label = {
                            Text("Number 2")
                        },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.White,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White
                        )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(20.dp)
                    )

                    Row {

                        listOf(
                            "+",
                            "-",
                            "*",
                            "/"
                        ).forEach { op ->

                            Button(
                                onClick = {

                                    calculate(
                                        number1,
                                        number2,
                                        op
                                    ) {
                                        result = it
                                    }
                                }
                            ) {

                                Text(op)
                            }

                            Spacer(
                                modifier =
                                    Modifier.width(8.dp)
                            )
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(20.dp)
                    )

                    Text(
                        text = result,
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }

    private fun calculate(
        n1: String,
        n2: String,
        operation: String,
        onResult: (String) -> Unit
    ) {

        scope.launch {
            if (calculatorService == null) {
                Toast.makeText(
                    this@MainActivity,
                    "Service chưa kết nối",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            try {

                val a =
                    n1.toDoubleOrNull() ?: 0.0

                val b =
                    n2.toDoubleOrNull() ?: 0.0

                val result =
                    withContext(Dispatchers.IO) {

                        when(operation) {

                            "+" ->
                                calculatorService
                                    ?.add(a, b)

                            "-" ->
                                calculatorService
                                    ?.subtract(a, b)

                            "*" ->
                                calculatorService
                                    ?.multiply(a, b)

                            "/" ->
                                calculatorService
                                    ?.divide(a, b)

                            else -> 0.0
                        }
                    }

                onResult(
                    "Result = $result"
                )

            } catch (e: Exception) {

                Toast.makeText(
                    this@MainActivity,
                    e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun bindCalculatorService() {

        val intent =
            Intent("BIND_CALCULATOR_SERVICE")

        intent.setPackage(
            "com.example.vcs_project11_service"
        )

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