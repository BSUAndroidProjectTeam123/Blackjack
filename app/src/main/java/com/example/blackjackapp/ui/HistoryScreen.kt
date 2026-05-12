package com.example.blackjackapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: BlackjackViewModel, navController: NavController) {
    // Получаем историю из БД как State
    val history by viewModel.gameHistory.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { navController.popBackStack() }) {
            Text("Назад")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(history) { game ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val dateString = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .format(Date(game.timestamp))
                        Text(text = "Дата: $dateString")
                        Text(text = "Счет: Игрок ${game.playerScore} - ${game.dealerScore} Дилер")
                        Text(text = "Результат: ${game.result}")
                    }
                }
            }
        }
    }
}