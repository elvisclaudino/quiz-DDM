package com.example.neyquiz

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.neyquiz.data.AppDatabase
import com.example.neyquiz.screens.LeaderboardScreen
import com.example.neyquiz.screens.MainMenuScreen
import com.example.neyquiz.screens.QuizScreen
import com.example.neyquiz.screens.NameInputScreen
import com.example.neyquiz.ui.theme.NeyquizTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "neyquiz-db").build()
        setContent {
            NeyquizTheme {
                val navController = rememberNavController()
                var playerName by remember { mutableStateOf("") }

                NavHost(navController = navController, startDestination = "name_input") {
                    composable(
                        "name_input",
                        enterTransition = { fadeIn(animationSpec = tween(700)) },
                        exitTransition = { fadeOut(animationSpec = tween(700)) }
                    ) {
                        NameInputScreen(navController) { name ->
                            playerName = name
                            navController.navigate("main_menu")
                        }
                    }
                    composable(
                        "main_menu",
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(700)) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(700)) }
                    ) {
                        MainMenuScreen(navController)
                    }
                    composable(
                        "quiz_screen",
                        enterTransition = { slideInVertically(initialOffsetY = { it }, animationSpec = tween(700)) },
                        exitTransition = { slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(700)) }
                    ) {
                        QuizScreen(navController, database, playerName)
                    }
                    composable(
                        "leaderboard_screen",
                        enterTransition = { slideInVertically(initialOffsetY = { it }, animationSpec = tween(700)) },
                        exitTransition = { slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(700)) }
                    ) {
                        LeaderboardScreen(navController, database)
                    }
                }
            }
        }
    }
}
