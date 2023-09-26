package com.example.lawdcm.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lawdcm.Utils
import com.example.lawdcm.models.CaseDetails
import com.example.lawdcm.singleton.registrarLoggedIn
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Objects

class DashBoardViewModel : ViewModel() {

    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference()

    var listOldCases = MutableLiveData<ArrayList<CaseDetails>>()
    var listNewHighPriority = MutableLiveData<ArrayList<CaseDetails>>()
    var listNewLowPriority = MutableLiveData<ArrayList<CaseDetails>>()
    var listOngoingHighPriority = MutableLiveData<ArrayList<CaseDetails>>()
    var listOngoingLowPriority = MutableLiveData<ArrayList<CaseDetails>>()
    var listBuffer = MutableLiveData<ArrayList<CaseDetails>>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun populateTodayCasesPriorityWise(judgeId : String, priorityCategory : String, callback: (ArrayList<String>) -> Unit){

        val courtId = registrarLoggedIn.courtId
        val curDate = Utils.getCurrentDate()
        var tmplist = ArrayList<String>()
        dbRef.child("casePool").child(courtId).child(judgeId).child(priorityCategory).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){

                        for (dates in snapshot.children){
                            if(dates.key == curDate){
                                tmplist = dates.value as ArrayList<String>
                                callback(tmplist)
                            }
                        }

                    }else {
                        callback(tmplist)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

}