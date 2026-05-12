package com.example.blackjackapp.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history")
data class GameHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val playerScore: Int,
    val dealerScore: Int,
    val result: String // Например: "Win", "Loss", "Draw"
)