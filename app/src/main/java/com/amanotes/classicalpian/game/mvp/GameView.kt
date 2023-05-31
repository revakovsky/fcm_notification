package com.amanotes.classicalpian.game.mvp

import moxy.MvpView
import moxy.viewstate.strategy.alias.SingleState

@SingleState
interface GameView : MvpView {

    fun showScore(score: Int)

}