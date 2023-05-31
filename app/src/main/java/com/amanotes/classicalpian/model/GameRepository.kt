package com.amanotes.classicalpian.model

interface GameRepository {

    suspend fun getScore(): Int?
    suspend fun saveScore(score: Int)
    suspend fun clearScore()

}