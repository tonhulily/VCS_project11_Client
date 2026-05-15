package com.example.vcs_project11_client.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun OperationButton(
    symbol: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier =
            Modifier.size(
                width = 56.dp,
                height = 56.dp
            ),
        shape =
            RoundedCornerShape(16.dp),
        colors =
            ButtonDefaults.buttonColors(containerColor = Color.White),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = symbol,
            color = Color(0xFF6D28D9),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}