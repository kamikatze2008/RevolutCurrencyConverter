package com.test.revolutcurrencyconverter

import android.app.Application
import com.test.revolutcurrenciesconverter.LoadCurrenciesUseCase
import com.test.revolutcurrenciesconverter.Repository
import com.test.revolutcurrencyconverter.data.CurrenciesApi
import com.test.revolutcurrencyconverter.data.DataSource
import com.test.revolutcurrencyconverter.data.DataSourceImpl
import com.test.revolutcurrencyconverter.data.RepositoryImpl
import com.test.revolutcurrencyconverter.fragment.CurrencyConverterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainApp : Application() {

    private val repositoryModule = module {
        single<Repository> { RepositoryImpl(get()) }
    }

    private val dataModule = module {
        single<DataSource> { DataSourceImpl(get()) }
    }

    private val useCaseModule = module {
        single { LoadCurrenciesUseCase(get()) }
    }

    private val networkModule = module {
        single<Converter.Factory> { MoshiConverterFactory.create() }

        single<CurrenciesApi> {
            Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org")
                .addConverterFactory(get())
                .build()
                .create(CurrenciesApi::class.java)
        }
    }

    private val viewModelModule = module {
        viewModel { CurrencyConverterViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                listOf(
                    repositoryModule,
                    dataModule,
                    useCaseModule,
                    networkModule,
                    viewModelModule
                )
            )

            androidContext(applicationContext)
        }
    }
}