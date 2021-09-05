package com.ldnhat.demosearchmap.network

import com.ldnhat.demosearchmap.model.ApiResponse
import com.ldnhat.demosearchmap.model.CountryDetail
import com.ldnhat.demosearchmap.utils.Constant
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApiService{

    @GET("map/country/province/all")
    fun findAllProvince() : Single<ApiResponse<MutableList<CountryDetail>>>

    @GET("map/country/district")
    fun findAllDistrict(@Query("provinceCode") provinceCode : String) : Single<ApiResponse<MutableList<CountryDetail>>>

    @GET("map/country/subdistrict")
    fun findAllSubDistrict(@Query("districtCode") districtCode : String) : Single<ApiResponse<MutableList<CountryDetail>>>
}

object CountryService{

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    val countryApiService = retrofit.create(CountryApiService::class.java)
}