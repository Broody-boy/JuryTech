package com.example.lawdcm.retrofit

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("Days_Group")
    var daysGroup : String? = null,
    @SerializedName("Lag_Group")
    var lagGroup : String? = null
)
