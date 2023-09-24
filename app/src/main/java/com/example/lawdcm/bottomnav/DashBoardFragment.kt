package com.example.lawdcm.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lawdcm.R
import com.example.lawdcm.databinding.FragmentDashBoardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashBoardFragment : Fragment() {


//    private lateinit var newHigh : ArrayList<>
    private lateinit var binding : FragmentDashBoardBinding
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashBoardBinding.inflate(layoutInflater, container, false)
        return binding.root


        binding.button.setOnClickListener {
            scheduleCase()
        }

    }

    private fun scheduleCase() {

        val dbJugdes = dbRef.child("judges")
        val dbJugdes2 = dbRef.child("judges")

        dbJugdes.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (judgeSnapshot in snapshot.children){

                    val cases = dbJugdes2.child(judgeSnapshot.key!!).child("assignedCases")


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




    }
}