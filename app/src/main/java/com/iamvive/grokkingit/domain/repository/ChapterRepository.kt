package com.iamvive.grokkingit.domain.repository

import com.iamvive.grokkingit.domain.model.Chapter
import kotlinx.coroutines.flow.Flow

interface ChapterRepository {
    fun getChapters(): Flow<List<Chapter>>
    fun getChapterById(id: Int): Flow<Chapter?>
    suspend fun markChapterAsCompleted(id: Int)
}
