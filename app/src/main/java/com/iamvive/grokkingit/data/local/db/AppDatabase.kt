package com.iamvive.grokkingit.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iamvive.grokkingit.data.local.db.dao.ProgressDao
import com.iamvive.grokkingit.data.local.db.entity.ProgressEntity

@Database(entities = [ProgressEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun progressDao(): ProgressDao
}
