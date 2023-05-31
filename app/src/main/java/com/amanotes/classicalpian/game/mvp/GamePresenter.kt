package com.amanotes.classicalpian.game.mvp

import com.amanotes.classicalpian.base.BasePresenter
import com.amanotes.classicalpian.model.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope

@InjectViewState
class GamePresenter(
    private val gameRepository: GameRepository
) : MvpPresenter<GameView>(), BasePresenter {

    fun onClearScore() {
        presenterScope.launch(Dispatchers.IO) {
            gameRepository.clearScore()
            getScore()
        }
    }

    fun saveMaxScore(maxScore: Int) {
        presenterScope.launch(Dispatchers.IO) {
            gameRepository.saveScore(maxScore)
        }
    }

    fun onGetMaxScore() {
        presenterScope.launch(Dispatchers.IO) { getScore() }
    }

    private suspend fun getScore() {
        val score = gameRepository.getScore()
        score?.let { viewState.showScore(it) } ?: viewState.showScore(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.detachView(viewState)
    }

}