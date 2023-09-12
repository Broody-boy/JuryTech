package com.example.lawdcm.models

data class CaseDetails(
    var caseId : String? = null,
    var courtId : String? = null,               //To sort the cases according to courts
    var caseName : String? = null,
    var caseType : String? = null,              //CIVIL or CRIMINAL
    var dateOfFiling : String? = null,
    var caseCategory : String? = null,
    var matterType : String? = null,            //URGENT or ORDINARY
    var causeOfAction : String? = null,
    var caseAct : String? = null,
    var caseActSection : String? = null,
    var applicantId : String? = null,
    var respondentId : String? = null,
    var priorityCategory : String? = null,      // enum(caps) -> NEW_HIGH/ NEW_LOW/ OLD/ ONGOING_HIGH/ ONGOING_LOW/ Buffer
    var judge : JudgeDetails? = null,           // this is a separate model class in another file, so see accordingly
)
