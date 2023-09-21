package com.example.lawdcm.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface BasePriorityNumberInterface {
    @GET("/predictGrp")
    fun getBasePriority(
        @Query("state_code") stateCode : String,
        @Query("dist_code") distcode : String,
        @Query("type_name") caseTypeId : String,
        @Query("judge_labels") judgeLabel : String
    ) : Call<ApiResponse>
}