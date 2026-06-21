package com.iamvive.grokkingit.presentation.chapters.ch1_binary_search

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamvive.grokkingit.domain.algorithm.BinarySearch
import com.iamvive.grokkingit.domain.model.SearchStep
import com.iamvive.grokkingit.domain.model.StepResult
import com.iamvive.grokkingit.domain.model.InteractiveStage
import com.iamvive.grokkingit.domain.model.VisualizerMode
import com.iamvive.grokkingit.domain.repository.ChapterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinarySearchViewModel @Inject constructor(
    private val repository: ChapterRepository
) : ViewModel() {

    val array = intArrayOf(1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25)
    val target = 17
    val steps = BinarySearch.generateBinarySearchSteps(array, target)

    var currentStepIndex by mutableIntStateOf(0)
    var isPlaying by mutableStateOf(false)
    var playbackSpeed by mutableFloatStateOf(1f)

    private var autoPlayJob: Job? = null

    // New Interactive Mode States
    var mode by mutableStateOf(VisualizerMode.INTERACTIVE)
    var interactiveStage by mutableStateOf(InteractiveStage.FEEDBACK)
    var selectedMidIndex by mutableIntStateOf(-1)
    var incorrectMidIndex by mutableIntStateOf(-1)
    var scoreCorrect by mutableIntStateOf(0)
    var scoreTotal by mutableIntStateOf(0)
    var interactiveFeedback by mutableStateOf("Welcome to Interactive Mode! You will play the role of the computer executing the Binary Search algorithm. Click 'Start Search' below to begin.")
    var feedbackIsCorrect by mutableStateOf(true)

    val currentStep: SearchStep
        get() = steps[currentStepIndex]

    fun nextStep() {
        if (currentStepIndex < steps.lastIndex) {
            currentStepIndex++
            if (currentStep.result == StepResult.FOUND) {
                markAsCompleted()
            }
        } else {
            isPlaying = false
        }
    }

    fun prevStep() {
        if (currentStepIndex > 0) {
            currentStepIndex--
        }
    }

    fun onNextClick() {
        pause()
        nextStep()
    }

    fun onPrevClick() {
        pause()
        prevStep()
    }

    private fun pause() {
        isPlaying = false
        autoPlayJob?.cancel()
    }

    fun togglePlayPause() {
        if (currentStepIndex == steps.lastIndex) {
            currentStepIndex = 0
        }
        isPlaying = !isPlaying
        if (isPlaying) {
            startAutoPlay()
        } else {
            autoPlayJob?.cancel()
        }
    }

    private fun startAutoPlay() {
        autoPlayJob?.cancel()
        autoPlayJob = viewModelScope.launch {
            while (isPlaying && currentStepIndex < steps.lastIndex) {
                delay((1000 / playbackSpeed).toLong())
                nextStep()
            }
            if (currentStepIndex == steps.lastIndex) {
                isPlaying = false
            }
        }
    }

    fun updateSpeed(speed: Float) {
        playbackSpeed = speed
        if (isPlaying) {
            startAutoPlay()
        }
    }

    private fun markAsCompleted() {
        viewModelScope.launch {
            repository.markChapterAsCompleted(1)
        }
    }

    fun setVisualizerMode(newMode: VisualizerMode) {
        mode = newMode
        pause()
        if (newMode == VisualizerMode.INTERACTIVE) {
            resetInteractiveGame()
        } else {
            currentStepIndex = 0
        }
    }

    fun resetInteractiveGame() {
        currentStepIndex = 0
        interactiveStage = InteractiveStage.FEEDBACK
        selectedMidIndex = -1
        incorrectMidIndex = -1
        scoreCorrect = 0
        scoreTotal = 0
        interactiveFeedback = "Welcome to Interactive Mode! You will play the role of the computer executing the Binary Search algorithm. Click 'Start Search' below to begin."
        feedbackIsCorrect = true
    }

    fun startInteractiveSearch() {
        currentStepIndex = 1
        interactiveStage = InteractiveStage.SELECT_MID
        selectedMidIndex = -1
        incorrectMidIndex = -1
        interactiveFeedback = ""
    }

    fun onMidSelected(index: Int) {
        val correctMid = steps[currentStepIndex].mid
        if (index == correctMid) {
            selectedMidIndex = index
            incorrectMidIndex = -1
            scoreCorrect++
            scoreTotal++
            interactiveStage = InteractiveStage.CHOOSE_DIRECTION
            feedbackIsCorrect = true
        } else {
            incorrectMidIndex = index
            scoreTotal++
            feedbackIsCorrect = false
        }
    }

    fun revealCorrectMid() {
        val correctMid = steps[currentStepIndex].mid
        selectedMidIndex = correctMid
        incorrectMidIndex = -1
        interactiveStage = InteractiveStage.CHOOSE_DIRECTION
    }

    fun onDirectionChosen(choice: StepResult) {
        val correctResult = steps[currentStepIndex].result
        val correctMid = steps[currentStepIndex].mid
        val guess = array[correctMid]
        
        feedbackIsCorrect = choice == correctResult
        if (feedbackIsCorrect) {
            scoreCorrect++
        }
        scoreTotal++

        interactiveFeedback = when (correctResult) {
            StepResult.FOUND -> {
                if (feedbackIsCorrect) {
                    "Correct! The middle element $guess is equal to the target $target. We have found the item!"
                } else {
                    "Not quite. The middle element $guess is exactly equal to the target $target, so we have found the item!"
                }
            }
            StepResult.TOO_HIGH -> {
                val nextHigh = correctMid - 1
                if (feedbackIsCorrect) {
                    "Correct! Since the guess $guess is greater than the target $target, the target must be in the left half. We update high = mid - 1 = $nextHigh."
                } else {
                    "Incorrect. Since the guess $guess is greater than the target $target, the target must be in the left half (smaller numbers). We update high = mid - 1 = $nextHigh."
                }
            }
            StepResult.TOO_LOW -> {
                val nextLow = correctMid + 1
                if (feedbackIsCorrect) {
                    "Correct! Since the guess $guess is less than the target $target, the target must be in the right half. We update low = mid + 1 = $nextLow."
                } else {
                    "Incorrect. Since the guess $guess is less than the target $target, the target must be in the right half (larger numbers). We update low = mid + 1 = $nextLow."
                }
            }
            else -> ""
        }
        
        interactiveStage = InteractiveStage.FEEDBACK
    }

    fun continueInteractive() {
        if (currentStepIndex < steps.lastIndex) {
            currentStepIndex++
            selectedMidIndex = -1
            incorrectMidIndex = -1
            val nextStepResult = steps[currentStepIndex].result
            if (nextStepResult == StepResult.NOT_FOUND) {
                interactiveFeedback = "The loop condition (low <= high) is now false because low (${steps[currentStepIndex].low}) is greater than high (${steps[currentStepIndex].high}). The target was not found in the array."
                feedbackIsCorrect = true
                interactiveStage = InteractiveStage.FEEDBACK
            } else {
                interactiveStage = InteractiveStage.SELECT_MID
            }
        } else {
            interactiveStage = InteractiveStage.FINISHED
            markAsCompleted()
        }
    }
}
