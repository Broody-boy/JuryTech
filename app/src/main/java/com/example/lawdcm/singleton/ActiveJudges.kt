package com.example.lawdcm.singleton

import androidx.lifecycle.MutableLiveData
import com.example.lawdcm.models.JudgeDetails

object ActiveJudges {
    init {

    }
    var activeJudgesList = MutableLiveData<ArrayList<JudgeDetails>>()
}