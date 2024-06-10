package com.example.neyquiz.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.neyquiz.ui.theme.NeyquizTheme

@Composable
fun MainMenuScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "NeyQuiz",
                    fontSize = 30.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(
                    onClick = {
                        isVisible = false
                        navController.navigate("quiz_screen")
                    },
                    modifier = Modifier.padding(16.dp)
                        .width(280.dp)
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.ui.graphics.Color.White.copy(alpha = 1f).run {
                        ButtonDefaults.outlinedButtonBorder
                    }
                ) {
                    Text("Jogar")
                }
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(
                    onClick = {
                        isVisible = false
                        navController.navigate("leaderboard_screen")
                    },
                    modifier = Modifier.padding(16.dp)
                        .width(280.dp)
                        .height(70.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.ui.graphics.Color.White.copy(alpha = 1f).run {
                        ButtonDefaults.outlinedButtonBorder
                    }
                ) {
                    Text("Leaderboard")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuScreenPreview() {
    val navController = rememberNavController()
    NeyquizTheme {
        MainMenuScreen(navController)
    }
}