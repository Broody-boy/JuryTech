package com.example.lawdcm.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lawdcm.models.CaseDetails

class AllCasesViewModel : ViewModel() {

    var listOldCases = MutableLiveData<ArrayList<CaseDetails>>()
    var listNewHighPriority = MutableLiveData<ArrayList<CaseDetails>>()
    var listNewLowPriority = MutableLiveData<ArrayList<CaseDetails>>()
    var listOngoingHighPriority = MutableLiveData<ArrayList<CaseDetails>>()
    var listOngoingLowPriority = MutableLiveData<ArrayList<CaseDetails>>()
    var listBuffer = MutableLiveData<ArrayList<CaseDetails>>()



}