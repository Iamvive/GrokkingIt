package com.iamvive.grokkingit.data.local.db.dao

import androidx.room.*
import com.iamvive.grokkingit.data.local.db.entity.ProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM progress")
    fun getAllProgress(): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM progress WHERE chapterId = :chapterId")
    fun getProgressByChapterId(chapterId: Int): Flow<ProgressEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: ProgressEntity): Unit
}
