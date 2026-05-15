package com.example.vcs_project11_client.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vcs_project11_client.ui.components.AppInput
import com.example.vcs_project11_client.ui.components.OperationButton
@Composable
fun HomeScreen(
    onCalculate: (
        String,
        String,
        String,
        (String) -> Unit
    ) -> Unit
) {
    var number1 by remember {
        mutableStateOf("")
    }
    var number2 by remember {
        mutableStateOf("")
    }
    var result by remember {
        mutableStateOf("0")
    }
    val backgroundGradient =
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF4F46E5),
                Color(0xFF7C3AED),
                Color(0xFFEC4899)
            )
        )
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(backgroundGradient)
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier.height(40.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Calculate,
                    contentDescription = null,
                    tint = Color(0xFFFFD166),
                    modifier = Modifier.size(54.dp)
                )
                Spacer(
                    modifier = Modifier.width(10.dp)
                )
                Text(
                    text = "Smart Calculator",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
            Spacer(
                modifier = Modifier.height(32.dp)
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                36.dp
                            )
                        )
                        .background(
                            Color.White.copy(
                                alpha = 0.14f
                            )
                        )
                        .padding(24.dp)
            ) {
                Column {
                    AppInput(
                        value = number1,
                        label =
                            "First Number"
                    ) {
                        number1 = it
                    }
                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )
                    AppInput(
                        value = number2,
                        label =
                            "Second Number"
                    ) {
                        number2 = it
                    }
                    Spacer(
                        modifier = Modifier.height(28.dp)
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                20.dp,
                                Alignment.CenterHorizontally
                            )
                    ) {
                        items(
                            listOf(
                                "+",
                                "-",
                                "×",
                                "÷"
                            )
                        ) { op ->
                            OperationButton(
                                symbol = op
                            ) {
                                val actualOp =
                                    when(op) {
                                        "×" -> "*"
                                        "÷" -> "/"
                                        else -> op
                                    }
                                onCalculate(
                                    number1,
                                    number2,
                                    actualOp
                                ) {
                                    result = it
                                }
                            }
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(32.dp)
                    )
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(30.dp)
                                )
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFFF6B6B),
                                            Color(0xFFFFA94D)
                                        )
                                    )
                                )
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 28.dp
                                ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "RESULT",
                                color =
                                    Color.White.copy(
                                        alpha = 0.85f
                                    ),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )
                            Text(
                                text = result,
                                color = Color.White,
                                fontSize = 40.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(22.dp)
                    )
                    Button(
                        onClick = {
                            number1 = ""
                            number2 = ""
                            result = "0"
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(58.dp),
                        shape =
                            RoundedCornerShape(24.dp),
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4338CA)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Backspace,
                            contentDescription = null,
                            tint = Color(0xFFF9FAFB)
                        )
                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )
                        Text(
                            text = "Clear",
                            color = Color(0xFFF9FAFB),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}