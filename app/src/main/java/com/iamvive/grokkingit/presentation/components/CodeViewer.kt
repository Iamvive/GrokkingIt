package com.iamvive.grokkingit.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CodeViewer(
    codeLines: List<String>,
    highlightLines: List<Int>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E24)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            codeLines.forEachIndexed { index, line ->
                val isHighlighted = highlightLines.contains(index)
                
                val rowBgColor by animateColorAsState(
                    targetValue = if (isHighlighted) Color(0x33FFC107) else Color.Transparent,
                    animationSpec = tween(200),
                    label = "rowBg"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBgColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = highlightKotlinCode(line),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = Color(0xFFE0E0E0)
                    )
                }
            }
        }
    }
}

private fun highlightKotlinCode(line: String): AnnotatedString {
    val keywords = setOf("fun", "var", "val", "while", "if", "else", "return")
    val types = setOf("IntArray", "Int")

    return buildAnnotatedString {
        val regex = Regex("(?<=\\b)|(?=\\b)|(?<=\\W)|(?=\\W)")
        val parts = line.split(regex)
        
        var isComment = false
        for (part in parts) {
            if (isComment) {
                withStyle(style = SpanStyle(color = Color(0xFF7F8C8D))) {
                    append(part)
                }
                continue
            }
            
            if (part == "//" || part.startsWith("//")) {
                isComment = true
                withStyle(style = SpanStyle(color = Color(0xFF7F8C8D))) {
                    append(part)
                }
                continue
            }
            
            when {
                part in keywords -> {
                    withStyle(style = SpanStyle(color = Color(0xFFF06292), fontWeight = FontWeight.Bold)) { // Pink keywords
                        append(part)
                    }
                }
                part in types -> {
                    withStyle(style = SpanStyle(color = Color(0xFF64B5F6), fontWeight = FontWeight.SemiBold)) { // Blue types
                        append(part)
                    }
                }
                part.toIntOrNull() != null -> {
                    withStyle(style = SpanStyle(color = Color(0xFFFFB74D))) { // Orange numbers
                        append(part)
                    }
                }
                else -> {
                    append(part)
                }
            }
        }
    }
}
