package com.amanotes.classicalpian.running.mvp

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

@AddToEndSingle
interface RunningView : MvpView {

    fun openWeb(url: String, isFirst: Boolean)
    fun passReferrerAndGadId(referrerString: String, gadId: String)
    fun runGame()

}