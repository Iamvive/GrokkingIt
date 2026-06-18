package com.iamvive.grokkingit.presentation.chapters

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamvive.grokkingit.domain.model.Chapter
import com.iamvive.grokkingit.domain.repository.ChapterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChapterDetailViewModel @Inject constructor(
    private val repository: ChapterRepository
) : ViewModel() {
    private val _chapter = MutableStateFlow<Chapter?>(null)
    val chapter = _chapter.asStateFlow()

    fun loadChapter(id: Int) {
        viewModelScope.launch {
            repository.getChapterById(id).collect {
                _chapter.value = it
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterDetailScreen(
    chapterId: Int,
    onBack: () -> Unit,
    onStartVisualizer: (Int) -> Unit,
    viewModel: ChapterDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(chapterId) {
        viewModel.loadChapter(chapterId)
    }

    val chapter by viewModel.chapter.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chapter Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        chapter?.let { ch ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(
                    text = ch.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ch.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Topics Covered:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                ch.topics.forEach { topic ->
                    Text(
                        text = "• $topic",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onStartVisualizer(ch.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Visualization")
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
        }
    }
}

// Helper for LaunchedEffect in Composable
@Composable
fun LaunchedEffect(key: Any?, block: suspend () -> Unit) {
    androidx.compose.runtime.LaunchedEffect(key) {
        block()
    }
}
