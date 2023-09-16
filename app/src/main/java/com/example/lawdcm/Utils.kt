package com.example.lawdcm

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.lawdcm.models.CaseDetails
import com.example.lawdcm.models.JudgeDetails
import com.example.lawdcm.models.RegistrarAuth
import com.example.lawdcm.singleton.ActiveJudges
import com.example.lawdcm.singleton.registrarLoggedIn
import com.example.lawdcm.viewmodels.AllCasesViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object Utils {

    fun loadLoggedInRegistrarDetails(registrarId : String , callback : LoggedInRegistrarDetailsCallBack){
        val dbreference = FirebaseDatabase.getInstance().getReference("registrars").child(registrarId)
        dbreference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val registrar = snapshot.getValue(RegistrarAuth::class.java)
                    if (registrar != null) {
                        registrarLoggedIn.courtId = registrar.courtId?:""
                        registrarLoggedIn.courtType = registrar.courtType?:""   //DISTRICT HIGH SUPREME
                        registrarLoggedIn.registrarDetails = registrar
                        registrarLoggedIn.dataFetchComplete.postValue(true)
                        callback.onRegistrarDetailsFetched(registrar)
                    }else{
                        callback.onRegistrarNotFound()
                    }
                }else{
                    callback.onRegistrarNotFound()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback.onError(error)
            }
        })
    }

    fun populateCasesIntoViewModel(courtId : String, callback: (ArrayList<CaseDetails>) -> Unit){
        val dbreference = FirebaseDatabase.getInstance().getReference("caseDetails").child(courtId).child("newCases")

        dbreference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val tmplist = ArrayList<CaseDetails>()
                for(snpsht in snapshot.children) {
                    tmplist.add(snpsht.getValue(CaseDetails::class.java)!!)
                }
                callback(tmplist)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun getJudgesListFromFirebase(courtId : String){
        val dbReference = FirebaseDatabase.getInstance().getReference("judges")
        val dbQuery = dbReference.orderByChild("courtId").equalTo(courtId)
        dbQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    ActiveJudges.activeJudgesList.value?.clear()

                    val tmplist = arrayListOf<JudgeDetails>()
                    for(snpsht in snapshot.children){
                        tmplist.add(snpsht.getValue(JudgeDetails::class.java)!!)
                    }
                    ActiveJudges.activeJudgesList.postValue(tmplist)
                }
                else
                    Log.d("err1", "err")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("err2", "err")
            }
        })
    }

    interface LoggedInRegistrarDetailsCallBack{
        fun onRegistrarDetailsFetched(registrar : RegistrarAuth)
        fun onRegistrarNotFound()
        fun onError(error : DatabaseError)
    }
}