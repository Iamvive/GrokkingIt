package com.iamvive.grokkingit.data.repository

import com.iamvive.grokkingit.data.local.db.dao.ProgressDao
import com.iamvive.grokkingit.data.local.db.entity.ProgressEntity
import com.iamvive.grokkingit.domain.model.Chapter
import com.iamvive.grokkingit.domain.repository.ChapterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChapterRepositoryImpl @Inject constructor(
    private val progressDao: ProgressDao
) : ChapterRepository {

    private val chapters = listOf(
        Chapter(
            id = 1,
            title = "Chapter 1: Binary Search",
            description = "Learn how to find items in a sorted list in logarithmic time.",
            topics = listOf("Sorted arrays", "Logarithmic time", "Low, high, and mid pointers")
        ),
        Chapter(
            id = 2,
            title = "Chapter 2: Selection Sort",
            description = "Understand how to sort a list by repeatedly finding the smallest element.",
            topics = listOf("Arrays vs. Linked Lists", "Big O notation", "Sorting smallest to largest")
        )
    )

    override fun getChapters(): Flow<List<Chapter>> {
        return progressDao.getAllProgress().map { progressList ->
            chapters.map { chapter ->
                val progress = progressList.find { it.chapterId == chapter.id }
                chapter.copy(isCompleted = progress?.completed ?: false)
            }
        }
    }

    override fun getChapterById(id: Int): Flow<Chapter?> {
        return progressDao.getProgressByChapterId(id).map { progress ->
            chapters.find { it.id == id }?.copy(isCompleted = progress?.completed ?: false)
        }
    }

    override suspend fun markChapterAsCompleted(id: Int) {
        progressDao.saveProgress(
            ProgressEntity(
                chapterId = id,
                completed = true,
                completedAt = System.currentTimeMillis()
            )
        )
    }
}
