package com.ldnhat.demosearchmap.repository

import com.ldnhat.demosearchmap.model.ApiResponse
import com.ldnhat.demosearchmap.model.CountryDetail
import com.ldnhat.demosearchmap.network.CountryApiService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.ReplaySubject

class CountryRepositoryImpl(private val countryApiService: CountryApiService) : CountryRepository {

    override fun findAllProvince(compositeDisposable: CompositeDisposable): ReplaySubject<Resource<ApiResponse<MutableList<CountryDetail>>>> {
        return object : NetworkBoundResource<ApiResponse<MutableList<CountryDetail>>, ApiResponse<MutableList<CountryDetail>>>(){
            override fun createCall(): Single<ApiResponse<MutableList<CountryDetail>>> = countryApiService.findAllProvince()
        }.handleSubject()
    }

    override fun findAllDistrict(
        compositeDisposable: CompositeDisposable,
        provinceCode: String
    ): ReplaySubject<Resource<ApiResponse<MutableList<CountryDetail>>>> {
        return object : NetworkBoundResource<ApiResponse<MutableList<CountryDetail>>, ApiResponse<MutableList<CountryDetail>>>(){
            override fun createCall(): Single<ApiResponse<MutableList<CountryDetail>>> = countryApiService.findAllDistrict(provinceCode)
        }.handleSubject()
    }

    override fun findAllSubDistrict(
        compositeDisposable: CompositeDisposable,
        districtCode: String
    ): ReplaySubject<Resource<ApiResponse<MutableList<CountryDetail>>>> {
        return object : NetworkBoundResource<ApiResponse<MutableList<CountryDetail>>, ApiResponse<MutableList<CountryDetail>>>(){
            override fun createCall(): Single<ApiResponse<MutableList<CountryDetail>>> = countryApiService.findAllSubDistrict(districtCode)
        }.handleSubject()
    }


}