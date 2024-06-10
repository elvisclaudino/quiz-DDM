package com.example.neyquiz.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import coil.compose.rememberImagePainter
import com.example.neyquiz.data.AppDatabase
import com.example.neyquiz.data.PlayerScore
import com.example.neyquiz.model.Question
import com.example.neyquiz.model.questions
import com.example.neyquiz.ui.theme.NeyquizTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun QuizScreen(navController: NavController, database: AppDatabase, playerName: String) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var shuffledQuestions by remember { mutableStateOf(questions.shuffled()) }
    var shuffledOptions by remember { mutableStateOf(shuffledQuestions[currentQuestionIndex].shuffledOptions()) }
    val coroutineScope = rememberCoroutineScope()
    var isVisible by remember { mutableStateOf(true) }
    var startTime by remember { mutableStateOf(0L) }
    var progress by remember { mutableStateOf(1f) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var backgroundColor by remember { mutableStateOf(Color.Transparent) }
    var textColor by remember { mutableStateOf(Color.White) }

    LaunchedEffect(currentQuestionIndex) {
        isVisible = true
        shuffledOptions = shuffledQuestions[currentQuestionIndex].shuffledOptions()
        startTime = System.currentTimeMillis()
        progress = 1f
        selectedOptionIndex = null
        backgroundColor = Color.Transparent
        textColor = Color.White
        coroutineScope.launch {
            while (progress > 0) {
                val elapsedTime = System.currentTimeMillis() - startTime
                val newProgress = 1f - elapsedTime.toFloat() / 10000f

                withContext(Dispatchers.Main) {
                    progress = newProgress
                }
                kotlinx.coroutines.delay(100)
            }
            if (progress <= 0) {
                handleAnswerSelected(navController, database, playerName, coroutineScope, "", shuffledQuestions[currentQuestionIndex], score, currentQuestionIndex, shuffledQuestions, startTime) { newScore, newIndex ->
                    score = newScore
                    currentQuestionIndex = newIndex
                }
            }
        }
    }

    val question = shuffledQuestions[currentQuestionIndex]

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "${currentQuestionIndex + 1}/${questions.size}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }

        AnimatedVisibility(
            visible = currentQuestionIndex < questions.size,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(700)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(700)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberImagePainter(question.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop,
                )

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    color = Color.White,
                    trackColor = Color.Black
                )

                Text(
                    text = question.text,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        shuffledOptions.forEachIndexed { index, option ->
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                        .background(
                            if (selectedOptionIndex == index) backgroundColor else Color.Transparent, RoundedCornerShape(8.dp)
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        text = "${('a' + index).toUpperCase()}.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedOptionIndex == index) textColor else Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedOptionIndex == index) textColor else Color.White,
                        modifier = Modifier
                            .clickable {
                                selectedOptionIndex = index
                                if (option == question.options[question.correctAnswerIndex]) {
                                    backgroundColor = Color.Green
                                    textColor = Color.Black
                                } else {
                                    backgroundColor = Color.Red
                                    textColor = Color.Black
                                }
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(1000)
                                    handleAnswerSelected(navController, database, playerName, coroutineScope, option, question, score, currentQuestionIndex, shuffledQuestions, startTime) { newScore, newIndex ->
                                        score = newScore
                                        currentQuestionIndex = newIndex
                                    }
                                }
                            }
                    )}
            }
        }
    }}
}

private fun handleAnswerSelected(
    navController: NavController,
    database: AppDatabase,
    playerName: String,
    coroutineScope: CoroutineScope,
    option: String,
    question: Question,
    currentScore: Int,
    currentQuestionIndex: Int,
    shuffledQuestions: List<Question>,
    startTime: Long,
    onScoreUpdate: (Int, Int) -> Unit
) {
    val elapsedTime = System.currentTimeMillis() - startTime
    var score = currentScore
    var questionIndex = currentQuestionIndex

    if (option == question.options[question.correctAnswerIndex]) {
        val questionScore = (1000 - elapsedTime / 10).coerceAtLeast(100)
        score += questionScore.toInt()
    }

    if (questionIndex < shuffledQuestions.size - 1) {
        questionIndex++
    } else {
        coroutineScope.launch {
            database.playerScoreDao().insert(PlayerScore(name = playerName, score = score))
            navController.navigate("leaderboard_screen")
        }
    }

    onScoreUpdate(score, questionIndex)
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


