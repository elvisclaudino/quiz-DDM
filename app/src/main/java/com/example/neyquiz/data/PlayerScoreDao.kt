package com.example.neyquiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlayerScoreDao {
    @Insert
    suspend fun insert(playerScore: PlayerScore)

    @Query("SELECT * FROM player_scores ORDER BY score DESC LIMIT 10")
    suspend fun getTopScores(): List<PlayerScore>
}