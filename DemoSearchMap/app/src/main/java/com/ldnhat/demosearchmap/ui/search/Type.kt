package com.ldnhat.demosearchmap.ui.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class Type(val type : String) : Parcelable {
    PROVINCE("province"),
    DISTRICT("district"),
    SUBDISTRICT("subdistrict")
}