package com.iamvive.grokkingit.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.iamvive.grokkingit.effect.EffectPlayer
import com.iamvive.grokkingit.presentation.chapters.ChapterDetailScreen
import com.iamvive.grokkingit.presentation.chapters.ch1_binary_search.BinarySearchScreen
import com.iamvive.grokkingit.presentation.home.HomeScreen

@Composable
fun AppNavGraph(effectPlayer: EffectPlayer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            HomeScreen(
                onChapterClick = { id ->
                    navController.navigate(ChapterDetail(id))
                }
            )
        }

        composable<ChapterDetail> { backStackEntry ->
            val route: ChapterDetail = backStackEntry.toRoute()
            ChapterDetailScreen(
                chapterId = route.chapterId,
                onBack = { navController.popBackStack() },
                onStartVisualizer = { id ->
                    navController.navigate(AlgorithmVisualizer(id, "binary_search"))
                }
            )
        }

        composable<AlgorithmVisualizer> { backStackEntry ->
            val route: AlgorithmVisualizer = backStackEntry.toRoute()
            // In a real app, we'd use algorithmId to decide which visualizer to show.
            // For MVP, we only have Binary Search.
            BinarySearchScreen(
                onBack = { navController.popBackStack() },
                effectPlayer = effectPlayer
            )
        }
    }
}
