package com.example.blackjackapp.ui // Поменяй на свой пакет, если нужно

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.blackjackapp.data.local.AppDatabase
import com.example.blackjackapp.data.local.GameHistory
import com.example.blackjackapp.data.remote.CardInfo
import com.example.blackjackapp.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Класс, хранящий текущее состояние игры для экрана
data class GameState(
    val deckId: String = "",
    val playerCards: List<CardInfo> = emptyList(),
    val dealerCards: List<CardInfo> = emptyList(),
    val playerScore: Int = 0,
    val dealerScore: Int = 0,
    val isGameOver: Boolean = false,
    val gameMessage: String = "",
    val isLoading: Boolean = false
)

class BlackjackViewModel(application: Application) : AndroidViewModel(application) {

    // Подключаем базу данных и сеть
    private val gameDao = AppDatabase.getDatabase(application).gameDao()
    private val api = RetrofitClient.apiService

    // Переменная состояния, за которой будет следить наш интерфейс Compose
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // Получаем историю игр из базы данных
    val gameHistory = gameDao.getAllHistory()

    // Начать новую игру
    fun startNewGame() {
        viewModelScope.launch {
            _gameState.value = GameState(isLoading = true)
            try {
                // 1. Берем новую колоду
                val deck = api.createNewDeck()
                _gameState.value = _gameState.value.copy(deckId = deck.deck_id)

                // 2. Раздаем по 2 карты
                drawCards(isPlayer = true, count = 2)
                drawCards(isPlayer = false, count = 2)

            } catch (e: Exception) {
                _gameState.value = _gameState.value.copy(
                    isLoading = false,
                    gameMessage = "Ошибка подключения: ${e.message}"
                )
            }
        }
    }

    // Взять карты (для игрока или дилера)
    fun hit() {
        if (_gameState.value.isGameOver) return
        viewModelScope.launch {
            drawCards(isPlayer = true, count = 1)

            // Если перебор — игра заканчивается
            if (_gameState.value.playerScore > 21) {
                endGame("Перебор! Вы проиграли.", "Loss")
            }
        }
    }

    // Игрок останавливается, ходит дилер
    fun stand() {
        if (_gameState.value.isGameOver) return
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(isLoading = true)

            var currentDealerScore = _gameState.value.dealerScore
            // Дилер обязан брать карты, пока у него меньше 17 очков
            while (currentDealerScore < 17) {
                drawCards(isPlayer = false, count = 1)
                currentDealerScore = _gameState.value.dealerScore
            }

            val playerScore = _gameState.value.playerScore

            // Определяем победителя
            when {
                currentDealerScore > 21 -> endGame("Дилер перебрал! Вы выиграли.", "Win")
                currentDealerScore > playerScore -> endGame("Дилер выиграл.", "Loss")
                currentDealerScore < playerScore -> endGame("Вы выиграли!", "Win")
                else -> endGame("Ничья!", "Draw")
            }
        }
    }

    // Вспомогательная функция для запроса карт и обновления счета
    private suspend fun drawCards(isPlayer: Boolean, count: Int) {
        val currentState = _gameState.value
        val response = api.drawCards(currentState.deckId, count)

        if (isPlayer) {
            val newCards = currentState.playerCards + response.cards
            _gameState.value = currentState.copy(
                playerCards = newCards,
                playerScore = calculateScore(newCards),
                isLoading = false
            )
        } else {
            val newCards = currentState.dealerCards + response.cards
            _gameState.value = currentState.copy(
                dealerCards = newCards,
                dealerScore = calculateScore(newCards),
                isLoading = false
            )
        }
    }

    // Логика подсчета очков
    private fun calculateScore(cards: List<CardInfo>): Int {
        var score = 0
        var aces = 0

        for (card in cards) {
            when (card.value) {
                "JACK", "QUEEN", "KING" -> score += 10
                "ACE" -> {
                    aces += 1
                    score += 11
                }
                else -> score += card.value.toIntOrNull() ?: 0
            }
        }

        // Если перебор, тузы считаются за 1 вместо 11
        while (score > 21 && aces > 0) {
            score -= 10
            aces -= 1
        }
        return score
    }

    // Конец игры и сохранение результата в Room
    private suspend fun endGame(message: String, result: String) {
        _gameState.value = _gameState.value.copy(
            isGameOver = true,
            gameMessage = message,
            isLoading = false
        )

        val historyEntry = GameHistory(
            playerScore = _gameState.value.playerScore,
            dealerScore = _gameState.value.dealerScore,
            result = result
        )
        gameDao.insertGameResult(historyEntry)
    }
}