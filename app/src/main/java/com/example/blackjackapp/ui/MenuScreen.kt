package com.example.blackjackapp.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController: NavController, viewModel: BlackjackViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Blackjack", fontSize = 36.sp, modifier = Modifier.padding(bottom = 32.dp))

        Button(
            onClick = {
                viewModel.startNewGame()
                navController.navigate("game")
            },
            modifier = Modifier.fillMaxWidth(0.6f).padding(8.dp)
        ) {
            Text("Новая игра")
        }

        Button(
            onClick = { navController.navigate("history") },
            modifier = Modifier.fillMaxWidth(0.6f).padding(8.dp)
        ) {
            Text("История игр")
        }
    }
}