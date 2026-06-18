package com.iamvive.grokkingit.domain.model

enum class StepResult {
    INITIAL, TOO_LOW, TOO_HIGH, FOUND, NOT_FOUND
}

data class SearchStep(
    val low: Int,
    val high: Int,
    val mid: Int,
    val result: StepResult,
    val description: String,
    val highlightLines: List<Int>
)

data class Chapter(
    val id: Int,
    val title: String,
    val description: String,
    val topics: List<String>,
    val isCompleted: Boolean = false
)
