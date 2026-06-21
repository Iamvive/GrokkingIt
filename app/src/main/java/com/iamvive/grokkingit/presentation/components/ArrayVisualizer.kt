package com.iamvive.grokkingit.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iamvive.grokkingit.domain.model.SearchStep
import com.iamvive.grokkingit.domain.model.StepResult
import com.iamvive.grokkingit.presentation.theme.CorrectGreen
import com.iamvive.grokkingit.presentation.theme.EliminatedGray
import com.iamvive.grokkingit.presentation.theme.HighlightPrimary

import androidx.compose.runtime.remember

@Composable
fun ArrayVisualizer(
    array: IntArray,
    step: SearchStep,
    modifier: Modifier = Modifier,
    interactiveMode: Boolean = false,
    selectedMidIndex: Int = -1,
    incorrectMidIndex: Int = -1,
    onElementClick: ((Int) -> Unit)? = null
) {
    val listState = rememberLazyListState()

    LaunchedEffect(step.mid, selectedMidIndex, step.low, step.high, interactiveMode) {
        val targetScrollIndex = if (interactiveMode) {
            if (selectedMidIndex != -1) selectedMidIndex else (step.low + step.high) / 2
        } else {
            if (step.mid != -1) step.mid else -1
        }
        if (targetScrollIndex != -1) {
            listState.animateScrollToItem(targetScrollIndex)
        }
    }

    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(array.toTypedArray()) { index, value ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Pointer labels (L, H, M)
                val pointerText = remember(step.low, step.high, step.mid, selectedMidIndex, interactiveMode) {
                    val labels = mutableListOf<String>()
                    if (index == step.low) labels.add("L")
                    if (index == step.high) labels.add("H")
                    if (interactiveMode) {
                        if (index == selectedMidIndex) labels.add("M")
                    } else {
                        if (index == step.mid) labels.add("M")
                    }
                    labels.joinToString(",")
                }

                Text(
                    text = pointerText,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        interactiveMode && index == selectedMidIndex -> HighlightPrimary
                        !interactiveMode && index == step.mid -> HighlightPrimary
                        index == step.low || index == step.high -> MaterialTheme.colorScheme.primary
                        else -> Color.Transparent
                    },
                    modifier = Modifier.height(18.dp)
                )

                ArrayElement(
                    value = value,
                    isMid = if (interactiveMode) index == selectedMidIndex else index == step.mid,
                    isEliminated = index < step.low || (step.high != -1 && index > step.high),
                    isFound = if (interactiveMode) {
                        index == selectedMidIndex && step.result == StepResult.FOUND
                    } else {
                        index == step.mid && step.result == StepResult.FOUND
                    },
                    isIncorrect = interactiveMode && index == incorrectMidIndex,
                    onClick = if (interactiveMode && index >= step.low && index <= step.high && onElementClick != null) {
                        { onElementClick(index) }
                    } else null
                )

                // Index indicator
                Text(
                    text = index.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun ArrayElement(
    value: Int,
    isMid: Boolean,
    isEliminated: Boolean,
    isFound: Boolean,
    isIncorrect: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isFound -> CorrectGreen
            isIncorrect -> MaterialTheme.colorScheme.errorContainer
            isMid -> HighlightPrimary
            isEliminated -> EliminatedGray
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(300),
        label = "color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isMid) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .scale(scale)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(
                2.dp,
                if (isIncorrect) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                RoundedCornerShape(8.dp)
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = if (isEliminated) Color.DarkGray else if (isIncorrect) MaterialTheme.colorScheme.onErrorContainer else Color.Unspecified
        )
    }
}
