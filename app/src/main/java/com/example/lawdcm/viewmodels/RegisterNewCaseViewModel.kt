package com.example.lawdcm.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lawdcm.models.CaseDetails

class RegisterNewCaseViewModel : ViewModel() {
    var _listingDataCollectingObject : CaseDetails = CaseDetails()
}