package com.iamvive.grokkingit.di

import com.iamvive.grokkingit.data.repository.ChapterRepositoryImpl
import com.iamvive.grokkingit.domain.repository.ChapterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindChapterRepository(
        chapterRepositoryImpl: ChapterRepositoryImpl
    ): ChapterRepository
}
