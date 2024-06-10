package com.example.neyquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.neyquiz.data.AppDatabase
import com.example.neyquiz.screens.LeaderboardScreen
import com.example.neyquiz.screens.MainMenuScreen
import com.example.neyquiz.screens.QuizScreen
import com.example.neyquiz.ui.theme.NeyquizTheme

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "neyquiz-db").build()
        setContent {
            NeyquizTheme {
                val navController = rememberNavController()
                var playerName by remember { mutableStateOf("") }

                NavHost(navController = navController, startDestination = "main_menu") {
                    composable("main_menu") {
                        MainMenuScreen(navController)
                    }
                    composable("quiz_screen") {
                        QuizScreen(navController, database, playerName)
                    }
                    composable("leaderboard_screen") {
                        LeaderboardScreen(navController, database)
                    }
                }
            }
        }
    }
}