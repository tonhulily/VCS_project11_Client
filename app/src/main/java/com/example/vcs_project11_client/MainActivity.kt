package com.example.vcs_project11_client

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
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

            val background =
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF7F9FC),
                        Color(0xFFEAF2FF),
                        Color(0xFFDDEBFF)
                    )
                )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
                    .padding(20.dp)
            ) {

                Column {

                    Spacer(
                        modifier =
                            Modifier.height(20.dp)
                    )

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.Calculate,
                            contentDescription = null,
                            tint =
                                Color(0xFF2563EB),
                            modifier =
                                Modifier.size(56.dp)
                        )

                        Spacer(
                            modifier =
                                Modifier.width(16.dp)
                        )

                        Text(
                            text =
                                "Smart Calculator",
                            fontSize = 32.sp,
                            fontWeight =
                                FontWeight.ExtraBold,
                            color =
                                Color(0xFF172554)
                        )
                    }
                    Spacer(
                        modifier =
                            Modifier.height(32.dp)
                    )

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(
                                28.dp
                            ),
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color.White
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(
                                    24.dp
                                )
                        ) {

                            OutlinedTextField(
                                value = number1,
                                onValueChange = {
                                    number1 = it
                                },
                                label = {
                                    Text("First Number")
                                },
                                keyboardOptions =
                                    KeyboardOptions(
                                        keyboardType =
                                            KeyboardType.Number
                                    ),
                                modifier =
                                    Modifier.fillMaxWidth(),
                                shape =
                                    RoundedCornerShape(
                                        18.dp
                                    )
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        18.dp
                                    )
                            )

                            OutlinedTextField(
                                value = number2,
                                onValueChange = {
                                    number2 = it
                                },
                                label = {
                                    Text("Second Number")
                                },
                                keyboardOptions =
                                    KeyboardOptions(
                                        keyboardType =
                                            KeyboardType.Number
                                    ),
                                modifier =
                                    Modifier.fillMaxWidth(),
                                shape =
                                    RoundedCornerShape(
                                        18.dp
                                    )
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        28.dp
                                    )
                            )

                            Row(
                                horizontalArrangement =
                                    Arrangement.SpaceBetween,
                                modifier =
                                    Modifier.fillMaxWidth()
                            ) {

                                listOf(
                                    "+",
                                    "-",
                                    "*",
                                    "/"
                                ).forEach { op ->

                                    Button(
                                        onClick = {

                                            if (
                                                op == "/" &&
                                                number2.toDoubleOrNull() == 0.0
                                            ) {

                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Cannot divide by zero",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                                return@Button
                                            }

                                            calculate(
                                                number1,
                                                number2,
                                                op
                                            ) {
                                                result = it
                                            }
                                        },
                                        shape =
                                            RoundedCornerShape(
                                                18.dp
                                            ),
                                        colors =
                                            ButtonDefaults.buttonColors(
                                                containerColor =
                                                    Color(
                                                        0xFF2563EB
                                                    )
                                            )
                                    ) {

                                        Text(
                                            text = op,
                                            fontSize = 24.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(28.dp)
                    )

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(
                                28.dp
                            ),
                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color.White
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(
                                    24.dp
                                )
                        ) {

                            Text(
                                text = "Result",
                                fontSize = 18.sp,
                                color =
                                    Color(0xFF64748B)
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        12.dp
                                    )
                            )

                            Text(
                                text =
                                    if (
                                        result.isEmpty()
                                    )
                                        "0"
                                    else
                                        result,
                                fontSize = 42.sp,
                                fontWeight =
                                    FontWeight.ExtraBold,
                                color =
                                    Color(0xFF1D4ED8)
                            )
                        }
                    }
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

            try {

                val a =
                    n1.toDoubleOrNull() ?: 0.0

                val b =
                    n2.toDoubleOrNull() ?: 0.0

                val result =
                    withContext(
                        Dispatchers.IO
                    ) {

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
                    "$result"
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
            Intent(
                "BIND_CALCULATOR_SERVICE"
            )

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