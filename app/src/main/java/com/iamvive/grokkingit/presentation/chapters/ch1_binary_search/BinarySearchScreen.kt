package com.iamvive.grokkingit.presentation.chapters.ch1_binary_search

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iamvive.grokkingit.effect.EffectPlayer
import com.iamvive.grokkingit.presentation.components.ArrayVisualizer
import com.iamvive.grokkingit.presentation.components.CodeViewer
import com.iamvive.grokkingit.presentation.components.StepControls
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BinarySearchScreen(
    onBack: () -> Unit,
    viewModel: BinarySearchViewModel = hiltViewModel(),
    effectPlayer: EffectPlayer
) {
    val hapticFeedback = LocalHapticFeedback.current

    LaunchedEffect(viewModel.currentStepIndex) {
        effectPlayer.playEffect(viewModel.currentStep.result, hapticFeedback)
    }

    val binarySearchCodeLines = listOf(
        "fun binarySearch(array: IntArray, target: Int): Int {",
        "    var low = 0",
        "    var high = array.size - 1",
        "    while (low <= high) {",
        "        val mid = (low + high) / 2",
        "        val guess = array[mid]",
        "        if (guess == target) {",
        "            return mid // Found!",
        "        }",
        "        if (guess > target) {",
        "            high = mid - 1 // Too high",
        "        } else {",
        "            low = mid + 1 // Too low",
        "        }",
        "    }",
        "    return -1 // Not found",
        "}"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Binary Search Visualizer") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Target: ${viewModel.target}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            ArrayVisualizer(
                array = viewModel.array,
                step = viewModel.currentStep,
                modifier = Modifier.fillMaxWidth().wrapContentHeight()
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = viewModel.currentStep.description,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Algorithm Execution",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(4.dp))

            CodeViewer(
                codeLines = binarySearchCodeLines,
                highlightLines = viewModel.currentStep.highlightLines,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            StepControls(
                isPlaying = viewModel.isPlaying,
                onTogglePlayPause = { viewModel.togglePlayPause() },
                onNext = { viewModel.onNextClick() },
                onPrev = { viewModel.onPrevClick() },
                playbackSpeed = viewModel.playbackSpeed,
                onSpeedChange = { viewModel.updateSpeed(it) }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
