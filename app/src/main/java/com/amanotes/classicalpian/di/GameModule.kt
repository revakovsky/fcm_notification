package com.amanotes.classicalpian.di

import com.amanotes.classicalpian.game.mvp.GamePresenter
import com.amanotes.classicalpian.model.GameRepository
import com.amanotes.classicalpian.model.GameRepositoryImpl
import org.koin.dsl.module

val gameModule = module {

    factory<GameRepository> { GameRepositoryImpl(dataStore = get()) }

    single { GamePresenter(gameRepository = get()) }

}