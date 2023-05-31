package com.amanotes.classicalpian.web.mvp

import com.amanotes.classicalpian.base.BasePresenter
import com.amanotes.classicalpian.model.MainRepository
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope

@InjectViewState
class WebPresenter(
    private val mainRepository: MainRepository
) : MvpPresenter<WebView>(), BasePresenter {

    fun onSaveWebData(url: String, urlSavingStatus: Boolean) {
        presenterScope.launch {
            mainRepository.saveRunningData(url, urlSavingStatus)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.detachView(viewState)
    }

}