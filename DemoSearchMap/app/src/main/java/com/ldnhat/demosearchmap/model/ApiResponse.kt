package com.ldnhat.demosearchmap.model

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(

    @SerializedName("code")
    val code : String,

    @SerializedName("message")
    val message : String,

    @SerializedName("result")
    val result : T
)