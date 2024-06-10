package com.example.neyquiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.neyquiz.ui.theme.NeyquizTheme

@Composable
fun MainMenuScreen(navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "NeyQuiz",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            Button(
                onClick = { navController.navigate("quiz_screen") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Jogar")
            }

            Button(
                onClick = { navController.navigate("leaderboard_screen") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Leaderboard")
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