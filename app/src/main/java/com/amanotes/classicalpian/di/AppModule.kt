package com.amanotes.classicalpian.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.amanotes.classicalpian.model.MainRepository
import com.amanotes.classicalpian.model.MainRepositoryImpl
import com.amanotes.classicalpian.model.source.ApiService
import com.amanotes.classicalpian.model.source.CryptoManager
import com.amanotes.classicalpian.model.source.CryptoManagerImpl
import com.amanotes.classicalpian.model.source.FileManager
import com.amanotes.classicalpian.model.source.FileManagerImpl
import com.amanotes.classicalpian.model.source.Loader
import com.amanotes.classicalpian.model.source.LoaderImpl
import com.amanotes.classicalpian.model.source.Referrer
import com.amanotes.classicalpian.running.mvp.RunningPresenter
import com.amanotes.classicalpian.web.mvp.WebPresenter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

val appModule = module {

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { androidContext().preferencesDataStoreFile(PREF_DATA_STORE) }
        )
    }

    factory<ApiService> {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        Retrofit.Builder()
            .baseUrl(ApiService.provideBaseUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient.Builder().build())
            .build()
            .create()
    }

    factory { Referrer(context = androidContext()) }

    factory<CryptoManager> { CryptoManagerImpl() }

    factory<FileManager> { FileManagerImpl(context = androidContext()) }

    factory<Loader> { LoaderImpl(apiService = get()) }

    factory<MainRepository> {
        MainRepositoryImpl(
            context = androidContext(),
            dataStore = get(),
            referrer = get()
        )
    }

    single { RunningPresenter(mainRepository = get()) }

    single { WebPresenter(mainRepository = get()) }

}

private const val PREF_DATA_STORE = "PREF_DATA_STORE"