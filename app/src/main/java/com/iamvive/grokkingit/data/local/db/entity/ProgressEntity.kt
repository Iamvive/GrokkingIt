package com.iamvive.grokkingit.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress")
data class ProgressEntity(
    @PrimaryKey val chapterId: Int,
    val completed: Boolean,
    val lastStepIndex: Int = 0,
    val completedAt: Long? = null
)
