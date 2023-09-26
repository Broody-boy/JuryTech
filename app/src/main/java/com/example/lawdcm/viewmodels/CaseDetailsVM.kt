package com.example.lawdcm.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lawdcm.models.CaseDetails
import com.example.lawdcm.models.PartyDetails

class CaseDetailsVM : ViewModel() {
    var caseDetailsCollectionObject : CaseDetails = CaseDetails()
    val collection : MutableLiveData<CaseDetails> = MutableLiveData()
    var applicantDetails : PartyDetails = PartyDetails()
    var respondentDetails : PartyDetails = PartyDetails()

}