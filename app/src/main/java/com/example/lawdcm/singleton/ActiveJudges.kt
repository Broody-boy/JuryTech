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





}