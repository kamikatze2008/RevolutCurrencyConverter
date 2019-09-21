package com.test.revolutcurrencyconverter

import android.app.Application
import com.test.revolutcurrencyconverter.data.CurrenciesApi
import com.test.revolutcurrencyconverter.fragment.CurrencyConverterViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainApp : Application() {

    private val networkModule = module {
        single { MoshiConverterFactory.create() }

        single {
            Retrofit.Builder()
                .baseUrl("https://revolut.duckdns.org")
                .addConverterFactory(get())
                .build()
                .create(CurrenciesApi::class.java)
        }
//        single {
//            Moshi.Builder()
//                .add(
//                    PolymorphicJsonAdapterFactory.of(RatesResponseObject::class.java, "rate")
//                        .withSubtype(Talk::class.java, ActionType.talk.name)
//                if you have more adapters, add them before this line:
//                .add(KotlinJsonAdapterFactory())
//                .build()
//        }
    }

    private val viewModelModule = module {
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
                    networkModule,
                    viewModelModule
                )
            )

            androidContext(applicationContext)
        }
    }
}