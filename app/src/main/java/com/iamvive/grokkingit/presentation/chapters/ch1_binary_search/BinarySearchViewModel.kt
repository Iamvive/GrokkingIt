package com.iamvive.grokkingit.presentation.chapters.ch1_binary_search

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamvive.grokkingit.domain.algorithm.BinarySearch
import com.iamvive.grokkingit.domain.model.SearchStep
import com.iamvive.grokkingit.domain.model.StepResult
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
}
