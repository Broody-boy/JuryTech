package com.example.lawdcm.singleton

class CaseCategoryList {
    val caseCategoryId = arrayListOf(
        734, 736, 853, 856, 977, 1822, 1892, 5070, 5098, 5943, 6716, 6743,
        284, 724, 1016, 1104, 1105, 1532, 1554, 1702, 1706, 1788, 1803, 1810,
        1937, 1943, 2045, 2073, 2090, 2243, 2290, 2299, 2302, 2303, 2327, 2338,
        2355, 2639, 2920, 2934, 3247, 3694, 3850, 4051, 4484, 4826, 4882, 4920,
        5003, 5032, 5085, 5188, 5539, 5559, 5671, 5707, 6049, 6748, 6769, 7014,
        7038, 7043, 7048, 7065, 7295, 7300, 7333, 7405
    )
    val caseCategoryName = arrayListOf(
        "CC - COOPERATIVE CASE",
        "GR CASE",
        "C.C. - CRIMINAL CASES",
        "S.C.C. - SUMMONS/SUMMARY CRIMINAL CASE",
        "CR. REG. CASE - CR. REGULAR",
        "NGR CASE",
        "BAIL APPLICATION",
        "C.C. - CONFISCATION CASE",
        "S.C.C. - SUMMARY CRIMINAL CASE",
        "CRIMINAL CASE",
        "ST - SUMMARY TRIAL/SESSIONS TRIAL",
        "OS - ORIGINAL SUIT",
        "ANTICIPATORY BAIL - ANTICIPATORY BAIL PETITION",
        "BAIL",
        "CHI - CHALLAN IPC",
        "CIVIL M.A.",
        "CIVIL M.J.C.",
        "COMPLAINT CASE",
        "COMPLAINT INDEX",
        "CR - CIVIL REVISION",
        "CR - CRIME CASE",
        "CR. CASE COMPLAINT (P)(CRIMINAL CASE) - CR. CASE COMPLAINT (P)",
        "CR. M. A. - CRIMINAL MISC. APPLICATION",
        "CR. MISC. CASES - CR. MISC",
        "CRI.BAIL APPLN. - BAIL APPLICATION",
        "CRI.M.A. - CRIMINAL MISC. APPLICATION",
        "CRIMINAL COMPLAINT - CR. CASE COMPLAINT (O)",
        "CRIMINAL MISC CASE (N)",
        "CRIMINAL MISC. - CRL. MISC.",
        "CRL.MISC. - CRIMINAL MISC.CASES",
        "CRLMP",
        "CRM - CRIMINAL MISC APPLICATION",
        "CRMA J",
        "CRMA S",
        "CS - CIVIL SUIT",
        "CT. CASES",
        "DOMESTIC VIOLENCE CASES",
        "EP - EXECUTION PETITION",
        "FDA - FINAL DECREE APPLICATION",
        "FINAL REPORT - FR",
        "HINDU MARRIAGE ACT",
        "MOTOR VEHICLE CLAIM ACT 1986",
        "M.V.C. - ACCIDENT CLAIM CASES U/R M.V",
        "MEX - MONEY EXECUTION",
        "MISC. CRI. APPLICATION",
        "MOTOR VEHICLE ACT",
        "NACT - 138 NIA ACT",
        "NGR CASE",
        "OMP (I) (COMM.) - COMMERCIAL ARBITRATION U/S 9",
        "OP TRAN - OP(TRANSFER)",
        "OTHER ACT",
        "P.C.R. - PRIVATE COMPLAINTS",
        "RCA CIVIL DJ ADJ - REG. CIVIL APPEAL FOR DJ ADJ",
        "RCT - RCT CASE",
        "REGULAR BAIL",
        "REMAND APPL POCSO",
        "SC.ST (PA )ACT - SC/ST (PA )ACT",
        "STC - SUMMARY TRIAL CASE",
        "SUCCESSION ACT.",
        "SUM - SUMMARY TRIAL",
        "SUMMON TRIAL",
        "SUMMONS FOR JUDGEMENT",
        "D. V. CASES - DOMESTIC VIOLENCE CASES",
        "T S ( DIV ) - T. S. ( DIV. )",
        "TRANSFER PETITION(N)",
        "TRAFFIC",
        "TRF - TRAFFIC CASES",
        "UCR - UNTRACE/CANCELLATION REPORT"
    )

    var caseCategoryList: List<Pair<Int, String>> = caseCategoryId.zip(caseCategoryName) { intValue, stringValue ->
        Pair(intValue, stringValue)
    }
}