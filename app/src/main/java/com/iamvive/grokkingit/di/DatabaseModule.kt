package com.iamvive.grokkingit.di

import android.content.Context
import androidx.room.Room
import com.iamvive.grokkingit.data.local.db.AppDatabase
import com.iamvive.grokkingit.data.local.db.dao.ProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "grokking_it_db"
        ).build()
    }

    @Provides
    fun provideProgressDao(database: AppDatabase): ProgressDao {
        return database.progressDao()
    }
}
