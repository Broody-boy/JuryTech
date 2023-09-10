package com.example.lawdcm.models

data class CaseDetails(
    var caseId : String? = null,
    var caseName : String? = null,
    var dateOfFiling : String? = null,
    var priorityCategory : String? = null,      // enum(caps) -> NEW_HIGH/ NEW_LOW/ OLD/ ONGOING_HIGH/ ONGOING_LOW/ Buffer
    var judge : judgeDetails? = null,           // this is a separate model class in another file, so see accordingly
    var caseCategory : String? = null,
    var causeOfAction : String? = null,
    var petitionerId : String? = null,
    var respondentId : String? = null,
)
