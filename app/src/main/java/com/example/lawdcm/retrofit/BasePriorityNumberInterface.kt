package com.example.lawdcm.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface BasePriorityNumberInterface {
    @GET("/predict")
    fun getBasePriority(
        @Query("stateCode") stateCode : Int,
        @Query("caseTypeId") caseTypeId : Int,
        @Query("judgeLabel") judgeLabel : Int
    ) : Call<ApiResponse>
}