package com.example.lawdcm.bottomnav

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.lawdcm.R
import com.example.lawdcm.Utils
import com.example.lawdcm.databinding.FragmentDashBoardBinding
import com.example.lawdcm.singleton.registrarLoggedIn
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.log

class DashBoardFragment : Fragment() {

    private lateinit var binding : FragmentDashBoardBinding
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashBoardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            scheduleCases("NEW_HIGH")
            scheduleCases("NEW_LOW")
        }


        for (i in 0 until binding.cgPie.childCount) {
            val chip = binding.cgPie.getChildAt(i) as Chip
            chip.setOnClickListener {
                changePhoto(chip.text.toString())
//                Toast.makeText(requireActivity(), chip.text.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        for (i in 0 until binding.cgBar.childCount) {
            val chip = binding.cgBar.getChildAt(i) as Chip
            chip.setOnClickListener {
                changeBar(chip.text.toString())
//                Toast.makeText(requireActivity(), chip.text.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeBar(year: String) {

        when(year){
            "2016"->binding.ivPie.setImageResource(R.drawable.bar2016)
            "2017"->binding.ivPie.setImageResource(R.drawable.bar2017)
            "2018"->binding.ivPie.setImageResource(R.drawable.bar2018)
        }

    }

    private fun changePhoto(year: String) {

        when(year){
            "2016"->binding.ivPie.setImageResource(R.drawable.pie2016)
            "2017"->binding.ivPie.setImageResource(R.drawable.pie2017)
            "2018"->binding.ivPie.setImageResource(R.drawable.pie2018)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleCases(priorityCategory: String) {

        var judgeWiseCasesHashMap : HashMap<String, ArrayList<String>>
        judgeWiseCasesHashMap = Paper.book().read(priorityCategory) ?: hashMapOf()


        for(judgeId in judgeWiseCasesHashMap.keys){

            val assignedCasesList = judgeWiseCasesHashMap.get(judgeId)  //this is ArrayList<CaseIds> for a particular judge

            convertToPriorityList(assignedCasesList!!){     //now, convert the above to ArrayList of Pair(caseId, its priority number)
                it.sortBy { it.second }   //sort by priority ascending

                scheduleToNextAvailableDate(priorityCategory, judgeId, it , it.size-1)  //and start scheduling from last

            }
        }
    }

    private fun convertToPriorityList(assignedCasesList: ArrayList<String>, callback : (ArrayList<Pair<String, Double>>) -> Unit) {
        var fetchedLimit = 0
        var fetchedList : ArrayList<Pair<String, Double>> = arrayListOf()

        for(caseId in assignedCasesList){
            Utils.fetchCaseDetailsFromCaseId(caseId){
                fetchedList.add(Pair(caseId, it.priorityNumber))
                fetchedLimit++

                if(fetchedLimit == assignedCasesList.size){
                    callback(fetchedList)
                }
            }
        }
    }

    private fun scheduleToNextAvailableDate(priorityCategory: String, judgeId: String, caseList : ArrayList<Pair<String,Double>>, noOfCases : Int) {

        //add judge care taking afterwards
        dbRef.child("casePool").child(registrarLoggedIn.courtId).child(judgeId).child(priorityCategory).addListenerForSingleValueEvent(object: ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()) {  //nothing there
                    Toast.makeText(requireActivity(), "switch 1", Toast.LENGTH_SHORT).show()
                    snapshot.ref.child(Utils.getCurrentDate()).setValue(arrayListOf(caseList[noOfCases].first))
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "switch 1 complete", Toast.LENGTH_SHORT).show()
                        }
                }
                else{   //some dates exist
                    var lastDate  = snapshot.children.last().key!!
                    var lastDateList  = snapshot.children.last().value as ArrayList<String>

                    if(lastDateList.size < 4){
                        lastDateList.add(caseList[noOfCases].first)
                        snapshot.ref.child(lastDate).setValue(lastDateList)
                        Log.d("daladata" , caseList[noOfCases].first)
                    }else{
                        lastDate = Utils.getNextCalenderDate(lastDate)
                        snapshot.ref.child(lastDate).setValue(arrayListOf(caseList[noOfCases].first))
                            .addOnSuccessListener {
                                Toast.makeText(requireActivity(), "switch 1 complete", Toast.LENGTH_SHORT).show()
                            }

                    }
                }

                if (noOfCases  >= 1){
                    scheduleToNextAvailableDate(priorityCategory , judgeId , caseList , noOfCases - 1)
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


    }
}