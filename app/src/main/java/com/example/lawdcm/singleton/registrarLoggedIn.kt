package com.example.lawdcm.singleton

import androidx.lifecycle.MutableLiveData
import com.example.lawdcm.models.RegistrarAuth

object registrarLoggedIn {
    var courtId : String = ""
    var courtType : String = ""   //DISTRICT HIGH SUPREME
    var registrarDetails : RegistrarAuth = RegistrarAuth()
    var dataFetchComplete =  MutableLiveData<Boolean>()
}