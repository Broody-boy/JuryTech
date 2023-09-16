package com.example.lawdcm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.lawdcm.models.CaseDetails
import com.example.lawdcm.models.JudgeDetails
import com.example.lawdcm.viewmodels.AllCasesViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object Utils {

    fun populateCasesIntoViewModel(cntxt: Context){
        val dbreference = FirebaseDatabase.getInstance().getReference("caseDetails").child("28398").child("newCases")

        dbreference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val tmplist = ArrayList<CaseDetails>()
                for(snpsht in snapshot.children) {
                    tmplist.add(snpsht.getValue(CaseDetails::class.java)!!)
                }

                val vmAllCasesViewModel = ViewModelProvider(cntxt as ViewModelStoreOwner)[AllCasesViewModel::class.java]

                vmAllCasesViewModel.listOfAllCases.postValue(tmplist)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}