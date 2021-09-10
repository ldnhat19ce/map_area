package com.ldnhat.demosearchmap.network

import com.ldnhat.demosearchmap.model.ApiResponse
import com.ldnhat.demosearchmap.model.CountryDetail
import io.reactivex.rxjava3.core.Single
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
