package com.example.neyquiz.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import coil.compose.rememberImagePainter
import com.example.neyquiz.data.AppDatabase
import com.example.neyquiz.data.PlayerScore
import com.example.neyquiz.model.questions
import com.example.neyquiz.ui.theme.NeyquizTheme
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(navController: NavController, database: AppDatabase, playerName: String) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var shuffledQuestions by remember { mutableStateOf(questions.shuffled()) }
    var shuffledOptions by remember { mutableStateOf(shuffledQuestions[currentQuestionIndex].shuffledOptions()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentQuestionIndex) {
        shuffledOptions = shuffledQuestions[currentQuestionIndex].shuffledOptions()
    }

    val question = shuffledQuestions[currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question.text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )

        Image(
            painter = rememberImagePainter(question.imageUrl),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        shuffledOptions.forEachIndexed { index, option ->
            Text(
                text = option,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        if (option == question.options[question.correctAnswerIndex]) {
                            score++
                        }
                        if (currentQuestionIndex < shuffledQuestions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            coroutineScope.launch {
                                database.playerScoreDao().insert(PlayerScore(name = playerName, score = score))
                                navController.navigate("leaderboard_screen")
                            }
                        }
                    }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    val navController = rememberNavController()
    val database = Room.inMemoryDatabaseBuilder(LocalContext.current, AppDatabase::class.java).build()
    NeyquizTheme {
        QuizScreen(navController, database, "Player")
    }
}