package com.iamvive.grokkingit.domain.algorithm

import com.iamvive.grokkingit.domain.model.SearchStep
import com.iamvive.grokkingit.domain.model.StepResult

object BinarySearch {
    fun generateBinarySearchSteps(array: IntArray, target: Int): List<SearchStep> {
        val steps = mutableListOf<SearchStep>()
        var low = 0
        var high = array.size - 1

        // Step 0: Initialization
        steps.add(
            SearchStep(
                low = low,
                high = high,
                mid = -1,
                result = StepResult.INITIAL,
                description = "Set low = 0 and high = ${array.size - 1}. Pointers cover the entire array initially.",
                highlightLines = listOf(1, 2)
            )
        )

        while (low <= high) {
            val mid = (low + high) / 2
            val guess = array[mid]

            if (guess == target) {
                steps.add(
                    SearchStep(
                        low = low,
                        high = high,
                        mid = mid,
                        result = StepResult.FOUND,
                        description = "Compare pointers: $low <= $high is true. Compute mid = $mid. guess ($guess) == target ($target). Return $mid!",
                        highlightLines = listOf(3, 4, 5, 6, 7)
                    )
                )
                return steps
            }

            if (guess > target) {
                val nextHigh = mid - 1
                steps.add(
                    SearchStep(
                        low = low,
                        high = high,
                        mid = mid,
                        result = StepResult.TOO_HIGH,
                        description = "Compare pointers: $low <= $high is true. Compute mid = $mid. guess ($guess) > target ($target) is too high. Adjust: high = mid - 1 = $nextHigh.",
                        highlightLines = listOf(3, 4, 5, 9, 10)
                    )
                )
                high = nextHigh
            } else {
                val nextLow = mid + 1
                steps.add(
                    SearchStep(
                        low = low,
                        high = high,
                        mid = mid,
                        result = StepResult.TOO_LOW,
                        description = "Compare pointers: $low <= $high is true. Compute mid = $mid. guess ($guess) < target ($target) is too low. Adjust: low = mid + 1 = $nextLow.",
                        highlightLines = listOf(3, 4, 5, 11, 12)
                    )
                )
                low = nextLow
            }
        }

        // Terminal step: Not found
        steps.add(
            SearchStep(
                low = low,
                high = high,
                mid = -1,
                result = StepResult.NOT_FOUND,
                description = "Loop finished: low > high. Target $target not found. Return -1.",
                highlightLines = listOf(15)
            )
        )
        return steps
    }
}
