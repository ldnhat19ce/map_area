package com.ldnhat.demosearchmap.di

import com.ldnhat.demosearchmap.network.CountryApiService
import com.ldnhat.demosearchmap.repository.CountryRepository
import com.ldnhat.demosearchmap.repository.CountryRepositoryImpl
import com.ldnhat.demosearchmap.utils.Constant
import com.ldnhat.demosearchmap.viewmodel.CountryViewModel
import com.ldnhat.demosearchmap.viewmodel.HomeViewModel
import com.ldnhat.demosearchmap.viewmodel.SearchViewModel
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel{ SearchViewModel() }
    viewModel{ HomeViewModel(androidApplication()) }
    viewModel { CountryViewModel(get()) }
}

val countryApiModule = module {
    fun provinceCountryApi(retrofit: Retrofit) : CountryApiService{
        return retrofit.create(CountryApiService::class.java)
    }

    single {
        provinceCountryApi(get())
    }
}

val netModule = module {

    fun provinceRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    single {
        provinceRetrofit()
    }
}

val repositoryModule = module {
    single<CountryRepository>{
        CountryRepositoryImpl(get())
    }
}