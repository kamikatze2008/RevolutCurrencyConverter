package com.test.revolutcurrencyconverter

import android.app.Application
import com.test.revolutcurrencyconverter.fragment.CurrencyConverterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApp : Application() {

    val viewModelModule = module {
        viewModel { CurrencyConverterViewModel() }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                listOf(
//                    appModule,
//                    repositoryModule,
//                    dataModule,
//                    useCaseModule,
                    viewModelModule
                )
            )

            androidContext(applicationContext)
        }
    }
}