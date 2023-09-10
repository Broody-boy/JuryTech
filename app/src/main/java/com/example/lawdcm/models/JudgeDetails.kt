package com.example.lawdcm.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class JudgeDetails (
    var judgeId : String? = null,
    var judgeName : String? = null,
    var courtType : String? = null,
    var courtId : String? = null,
)