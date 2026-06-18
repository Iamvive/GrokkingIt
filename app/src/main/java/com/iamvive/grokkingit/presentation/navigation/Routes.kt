package com.iamvive.grokkingit.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class ChapterDetail(val chapterId: Int)

@Serializable
data class AlgorithmVisualizer(val chapterId: Int, val algorithmId: String)
