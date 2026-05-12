package com.example.blackjackapp.ui // Твой пакет

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import com.example.blackjackapp.data.remote.CardInfo

@Composable
fun GameScreen(viewModel: BlackjackViewModel) {
    // Подписываемся на изменения состояния из ViewModel
    val state by viewModel.gameState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- Зона Дилера ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Счет дилера: ${state.dealerScore}")
            CardRow(cards = state.dealerCards)
        }

        // --- Информационный центр ---
        if (state.isLoading) {
            Text(text = "Раздаем карты...")
        } else if (state.gameMessage.isNotEmpty()) {
            Text(text = state.gameMessage)
        }

        // --- Зона Игрока ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Твой счет: ${state.playerScore}")
            CardRow(cards = state.playerCards)
        }

        // --- Панель управления (Тач-интерфейс) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.hit() },
                enabled = !state.isGameOver && !state.isLoading
            ) {
                Text("Hit (Взять)")
            }

            Button(
                onClick = { viewModel.stand() },
                enabled = !state.isGameOver && !state.isLoading
            ) {
                Text("Stand (Хватит)")
            }

            // Если игра окончена, показываем кнопку рестарта
            if (state.isGameOver) {
                Button(onClick = { viewModel.startNewGame() }) {
                    Text("Заново")
                }
            }
        }
    }
}

@Composable
fun CardRow(cards: List<CardInfo>) {
    LazyRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy((-30).dp) // Карты будут идти немного внахлест
    ) {
        items(cards) { card ->
            AsyncImage(
                model = card.image,
                contentDescription = "${card.value} of ${card.suit}",
                modifier = Modifier
                    .width(100.dp)
                    .height(140.dp)
            )
        }
    }
}