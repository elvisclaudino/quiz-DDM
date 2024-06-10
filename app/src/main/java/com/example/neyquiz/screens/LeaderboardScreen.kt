package com.example.neyquiz.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.neyquiz.data.AppDatabase
import com.example.neyquiz.data.PlayerScore
import com.example.neyquiz.ui.theme.NeyquizTheme
import kotlinx.coroutines.launch

@Composable
fun LeaderboardScreen(navController: NavController, database: AppDatabase) {
    var scores by remember { mutableStateOf(emptyList<PlayerScore>()) }
    val coroutineScope = rememberCoroutineScope()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            scores = database.playerScoreDao().getTopScores()
            isVisible = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Leaderboard", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))

        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                scores.forEach { score ->
                    Text(text = "${score.name}: ${score.score}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    val navController = rememberNavController()
    val database = Room.inMemoryDatabaseBuilder(LocalContext.current, AppDatabase::class.java).build()
    NeyquizTheme {
        LeaderboardScreen(navController, database)
    }
}