package com.example.lawdcm.models

data class PartyDetails(
    var partyId : String? = null,
    var name : String? = null,
    var dateOfBirth : String? = null,
    var gender : String? = null,
    var relativeName : String? = null,
    var relativeRelation : String? = null,
    var religion : String? = null,
    var caste : String? = null,
    var contact : AuxContact = AuxContact(),
    var address : AuxAddress = AuxAddress(),
    var associatedCaseId : String? = null
)

data class AuxContact(
    var emailId : String? = null,
    var phoneNumber : String? = null
)

data class AuxAddress(
    var address : String? = null,
    var state : String? = null,
    var district : String? = null,
    var taluka : String? = null,
    var pinCode : String? = null,
)