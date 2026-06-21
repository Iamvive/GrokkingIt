package com.iamvive.grokkingit.presentation.chapters.ch1_binary_search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iamvive.grokkingit.effect.EffectPlayer
import com.iamvive.grokkingit.presentation.components.ArrayVisualizer
import com.iamvive.grokkingit.presentation.components.CodeViewer
import com.iamvive.grokkingit.presentation.components.StepControls
import com.iamvive.grokkingit.presentation.theme.CorrectGreen
import com.iamvive.grokkingit.domain.model.InteractiveStage
import com.iamvive.grokkingit.domain.model.VisualizerMode
import com.iamvive.grokkingit.domain.model.StepResult
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BinarySearchScreen(
    onBack: () -> Unit,
    viewModel: BinarySearchViewModel = hiltViewModel(),
    effectPlayer: EffectPlayer
) {
    val hapticFeedback = LocalHapticFeedback.current
    var showCodeOnly by remember { mutableStateOf(false) }

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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Mode Selector Tab
            TabRow(
                selectedTabIndex = if (viewModel.mode == VisualizerMode.INTERACTIVE) 0 else 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Tab(
                    selected = viewModel.mode == VisualizerMode.INTERACTIVE,
                    onClick = { viewModel.setVisualizerMode(VisualizerMode.INTERACTIVE) },
                    text = { Text("Interactive Quiz") }
                )
                Tab(
                    selected = viewModel.mode == VisualizerMode.VISUALIZER,
                    onClick = { viewModel.setVisualizerMode(VisualizerMode.VISUALIZER) },
                    text = { Text("Visualizer Mode") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.mode == VisualizerMode.VISUALIZER && showCodeOnly) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Algorithm Source Code",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Code Only", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = showCodeOnly,
                            onCheckedChange = { checked -> showCodeOnly = checked }
                        )
                    }
                }

                CodeViewer(
                    codeLines = binarySearchCodeLines,
                    highlightLines = viewModel.currentStep.highlightLines,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(480.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Text(
                    text = "Target: ${viewModel.target}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                ArrayVisualizer(
                    array = viewModel.array,
                    step = viewModel.currentStep,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    interactiveMode = viewModel.mode == VisualizerMode.INTERACTIVE,
                    selectedMidIndex = viewModel.selectedMidIndex,
                    incorrectMidIndex = viewModel.incorrectMidIndex,
                    onElementClick = { idx ->
                        if (viewModel.interactiveStage == InteractiveStage.SELECT_MID) {
                            viewModel.onMidSelected(idx)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (viewModel.mode == VisualizerMode.VISUALIZER) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Algorithm Execution",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Code Only", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(
                                checked = showCodeOnly,
                                onCheckedChange = { checked -> showCodeOnly = checked }
                            )
                        }
                    }

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

                    CodeViewer(
                        codeLines = binarySearchCodeLines,
                        highlightLines = viewModel.currentStep.highlightLines,
                        modifier = Modifier.height(280.dp)
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
                } else {
                    InteractiveQuizPanel(
                        viewModel = viewModel,
                        binarySearchCodeLines = binarySearchCodeLines
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun InteractiveQuizPanel(
    viewModel: BinarySearchViewModel,
    binarySearchCodeLines: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (viewModel.interactiveStage) {
                InteractiveStage.FEEDBACK -> {
                    if (viewModel.feedbackIsCorrect) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                }
                InteractiveStage.FINISHED -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewModel.interactiveStage != InteractiveStage.FINISHED && viewModel.scoreTotal > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Interactive Challenge",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Score: ${viewModel.scoreCorrect}/${viewModel.scoreTotal}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            when (viewModel.interactiveStage) {
                InteractiveStage.SELECT_MID -> {
                    Text(
                        text = "Identify the Middle Element",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We are currently searching the range from index ${viewModel.currentStep.low} to ${viewModel.currentStep.high}. Calculate the index of the middle element and tap it in the array above.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Formula: (low + high) / 2 = (${viewModel.currentStep.low} + ${viewModel.currentStep.high}) / 2",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    if (viewModel.incorrectMidIndex != -1) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Index ${viewModel.incorrectMidIndex} is incorrect. Please try again or reveal the correct mid index.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.revealCorrectMid() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Reveal Correct Mid")
                        }
                    }
                }

                InteractiveStage.CHOOSE_DIRECTION -> {
                    Text(
                        text = "Decide the Next Search Step",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val selectedValue = viewModel.array[viewModel.selectedMidIndex]
                    Text(
                        text = "You selected mid index ${viewModel.selectedMidIndex} (value: $selectedValue). The target value is ${viewModel.target}.\n\nCompare the value at mid to the target:",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.onDirectionChosen(StepResult.TOO_HIGH) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Target is smaller (Go Left/Previous)")
                        }
                        Button(
                            onClick = { viewModel.onDirectionChosen(StepResult.TOO_LOW) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text("Target is larger (Go Right/Next)")
                        }
                        Button(
                            onClick = { viewModel.onDirectionChosen(StepResult.FOUND) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("This is the Target! (Found)")
                        }
                    }
                }

                InteractiveStage.FEEDBACK -> {
                    if (viewModel.currentStepIndex == 0) {
                        Text(
                            text = "Binary Search Quiz 🧠",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Play the role of the computer executing the Binary Search algorithm. Before you begin, here is what you need to know:",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Prerequisites:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        
                        ConceptItem(
                            title = "• Sorted Array Required",
                            description = "Binary search only works on sorted collections. If the elements are out of order, comparing the middle element won't tell us which half to eliminate, rendering the algorithm useless.",
                            textColor = MaterialTheme.colorScheme.onSurface
                        )
                        ConceptItem(
                            title = "• Direct Access (O(1) indexing)",
                            description = "We must be able to lookup any element by index instantly. This makes arrays perfect for binary search, whereas linked lists are inefficient because we can't jump directly to the middle.",
                            textColor = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Algorithm Flowchart:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowStep("1. Initialize Bounds", "Start with search range covering the entire array (low = 0, high = size - 1).")
                        Text(
                            text = "▼",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        FlowStep("2. Calculate Middle Index", "Find the mid-point: mid = (low + high) / 2.")
                        Text(
                            text = "▼",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        FlowStep("3. Compare Value at Mid", "Check how array[mid] compares to the target value.")
                        Text(
                            text = "▼",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        FlowStep("4. Adjust Bounds", "Based on comparison:\n• Equal -> Target found!\n• Too Large -> Narrow range left (high = mid - 1)\n• Too Small -> Narrow range right (low = mid + 1)")
                        Text(
                            text = "▼",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                        FlowStep("5. Loop or Terminate", "If range is still valid (low <= high), repeat from Step 2. Otherwise, stop (Target not found).")

                        Spacer(modifier = Modifier.height(20.dp))
                    } else {
                        val statusText = if (viewModel.feedbackIsCorrect) "Correct! 🎉" else "Incorrect ❌"
                        Text(
                            text = statusText,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (viewModel.feedbackIsCorrect) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            },
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.interactiveFeedback,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (viewModel.feedbackIsCorrect) {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            } else {
                                MaterialTheme.colorScheme.onErrorContainer
                            },
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Button(
                        onClick = {
                            if (viewModel.currentStepIndex == 0) {
                                viewModel.startInteractiveSearch()
                            } else {
                                viewModel.continueInteractive()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (viewModel.currentStepIndex == 0) {
                                MaterialTheme.colorScheme.primary
                            } else if (viewModel.feedbackIsCorrect) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    ) {
                        Text(if (viewModel.currentStepIndex == 0) "Start Search" else "Continue")
                    }
                }

                InteractiveStage.FINISHED -> {
                    Text(
                        text = "Challenge Completed! 🏆",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You've successfully completed the Binary Search puzzle! Here are your results:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "Final Score: ${viewModel.scoreCorrect} / ${viewModel.scoreTotal} (${((viewModel.scoreCorrect.toFloat() / viewModel.scoreTotal) * 100).toInt()}% Correct)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Core Programming Concepts Explained",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    ConceptItem(
                        title = "1. Pointer Management",
                        description = "Binary Search uses two pointers (low and high) to define the active range of the array being searched. Each incorrect guess allows us to move either the low or high pointer, narrowing our search area by half.",
                        textColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    ConceptItem(
                        title = "2. The Loop Condition",
                        description = "The loop runs as long as low <= high. If the target is not present in the array, the low and high pointers will eventually pass each other (low > high), terminating the loop and returning -1.",
                        textColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    ConceptItem(
                        title = "3. O(log n) Complexity",
                        description = "By dividing the remaining search array in half each time, binary search runs in logarithmic time. For an array of 13 elements, it takes at most 4 steps to find the item or confirm it is missing!",
                        textColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "When to Use vs. When NOT to Use",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "✅ Use When:",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = CorrectGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• Dataset is sorted.\n• Fast lookup is critical (O(log n)).\n• Contiguous memory (Arrays).\n• Read-heavy datasets.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "❌ Do NOT Use:",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• Dataset is unsorted.\n• Structures without O(1) indexing (Linked Lists).\n• Very small datasets.\n• Frequent insertions or deletions.",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Binary Search Source Code",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    CodeViewer(
                        codeLines = binarySearchCodeLines,
                        highlightLines = emptyList(),
                        modifier = Modifier.height(280.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.resetInteractiveGame() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Try Again")
                    }
                }
            }
        }
    }
}

@Composable
fun ConceptItem(
    title: String,
    description: String,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = textColor.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun FlowStep(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
