package com.example.lawdcm.models

data class RegistrarAuth(

    var name : String? = null,
    var registrarId :String? = null,
    var courtType : String? = null,
    var courtId : String? = null,
    var officialEmail:String? = null,
    var passKey: String? = null,
    var imageUrl: String? = null
)
