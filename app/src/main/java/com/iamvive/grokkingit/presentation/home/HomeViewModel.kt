package com.iamvive.grokkingit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamvive.grokkingit.domain.model.Chapter
import com.iamvive.grokkingit.domain.repository.ChapterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ChapterRepository
) : ViewModel() {
    val chapters: StateFlow<List<Chapter>> = repository.getChapters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
