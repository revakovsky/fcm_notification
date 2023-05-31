package com.amanotes.classicalpian.running.mvp

import com.amanotes.classicalpian.base.BasePresenter
import com.amanotes.classicalpian.model.MainRepository
import com.onesignal.OneSignal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope

@InjectViewState
class RunningPresenter(
    private val mainRepository: MainRepository
) : MvpPresenter<RunningView>(), BasePresenter {

    fun onLoadUrl() {
        presenterScope.launch(Dispatchers.IO) {
            val (passedUrl, isFirst) = mainRepository.getRunningData()

            if (passedUrl != null && isFirst != null) {
                if (passedUrl == "GAME_URL") viewState.runGame()
                else if (passedUrl.isNotEmpty()) viewState.openWeb(passedUrl, isFirst)
            } else createUrl()
        }
    }

    private suspend fun createUrl() {
        val referrerString = mainRepository.getReferrerString()
        val gadId = mainRepository.getGadId()
        OneSignal.setExternalUserId(gadId)

        if (referrerString.isNotBlank()) {
            viewState.passReferrerAndGadId(
                referrerString,
                gadId
            )
        } else viewState.runGame()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.detachView(viewState)
    }

}