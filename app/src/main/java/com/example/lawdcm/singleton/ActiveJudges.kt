package com.example.lawdcm.singleton

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.lawdcm.models.JudgeDetails
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object ActiveJudges {

    init {

    }

    var activeJudgesList = MutableLiveData<ArrayList<JudgeDetails>>()
    private val dbReference = FirebaseDatabase.getInstance().getReference("judges")


    fun getJudgesListFromFirebase(courtId : String){
        val dbQuery = dbReference.orderByChild("courtId").equalTo(courtId)
        dbQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    activeJudgesList.value?.clear()

                    val tmplist = arrayListOf<JudgeDetails>()
                    for(snpsht in snapshot.children){
                        tmplist.add(snpsht.getValue(JudgeDetails::class.java)!!)
                    }
                    activeJudgesList.postValue(tmplist)
                }
                else
                    Log.d("err1", "err")
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("err2", "err")
            }
        })
    }
}