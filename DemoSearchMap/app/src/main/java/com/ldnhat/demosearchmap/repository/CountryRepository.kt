package com.ldnhat.demosearchmap.repository

import com.ldnhat.demosearchmap.model.ApiResponse
import com.ldnhat.demosearchmap.model.CountryDetail
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.ReplaySubject

interface CountryRepository {

    fun findAllProvince(compositeDisposable: CompositeDisposable) : ReplaySubject<Resource<ApiResponse<MutableList<CountryDetail>>>>

    fun findAllDistrict(compositeDisposable: CompositeDisposable, provinceCode : String)
        : ReplaySubject<Resource<ApiResponse<MutableList<CountryDetail>>>>

    fun findAllSubDistrict(compositeDisposable: CompositeDisposable, districtCode : String)
        : ReplaySubject<Resource<ApiResponse<MutableList<CountryDetail>>>>
}