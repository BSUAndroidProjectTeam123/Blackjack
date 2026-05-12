package com.example.blackjackapp.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert
    suspend fun insertGameResult(game: GameHistory)

    // Flow позволяет UI автоматически обновляться при изменении данных
    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<GameHistory>>
}