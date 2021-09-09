package com.ldnhat.demosearchmap.di

import com.ldnhat.demosearchmap.viewmodel.HomeViewModel
import com.ldnhat.demosearchmap.viewmodel.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ SearchViewModel() }
    viewModel{ HomeViewModel(androidApplication()) }
}