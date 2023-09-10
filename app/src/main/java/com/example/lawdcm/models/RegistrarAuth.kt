package com.example.lawdcm.models

data class RegistrarAuth(

    var registrarId :String? = null,
    var courtType : String? = null,
    var courtId : String? = null,
    var officialEmail:String? = null,
    var passKey: String? = null
)
