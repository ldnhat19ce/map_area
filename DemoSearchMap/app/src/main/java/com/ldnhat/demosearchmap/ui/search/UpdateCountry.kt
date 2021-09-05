package com.ldnhat.demosearchmap.ui.search

import com.ldnhat.demosearchmap.model.CountryDetail

interface UpdateCountry{
    fun dataListener(type: Type, countryDetail: CountryDetail)
}