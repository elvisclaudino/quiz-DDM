package com.example.neyquiz.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.neyquiz.ui.theme.NeyquizTheme

@Composable
fun NameInputScreen(navController: NavController, onNameEntered: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

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
                    text = "Insira seu nome",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.padding(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            if (name.isNotBlank()) {
                                onNameEntered(name)
                                navController.navigate("main_menu")
                            } else {
                                Toast.makeText(context, "Por favor, insira um nome válido", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                )
            }

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(
                    onClick = {
                        keyboardController?.hide()
                        if (name.isNotBlank()) {
                            isVisible = false
                            onNameEntered(name)
                            navController.navigate("main_menu")
                        } else {
                            Toast.makeText(context, "Por favor, insira um nome válido", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                        .width(200.dp)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        Color.Transparent,
                        contentColor = Color.White,
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.5f).run {
                        ButtonDefaults.outlinedButtonBorder
                    }
                ) {
                    Text("Entrar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NameInputScreenPreview() {
    val navController = rememberNavController()
    NeyquizTheme {
        NameInputScreen(navController = navController) {}
    }
}
