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
import com.example.lawdcm.Utils
import com.example.lawdcm.databinding.FragmentDashBoardBinding
import com.example.lawdcm.singleton.registrarLoggedIn
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


    //    private lateinit var newHigh : ArrayList<>
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleCases(priorityCategory: String) {

//        val priorityCategoryDUMMY = "NEW_HIGH"
//        val judgeIdDUMMY = "808080"
//        Paper.book().write(priorityCategoryDUMMY, arrayListOf("1","2","3","4","5","6"))

//        [(2, 3.3049999999999997), (4, 1.89), (5, 1.66), (1, 1.53), (6, 1.299), (3, 1.105)]




                                           //caseID  JudgeID
        val newHighIdList : ArrayList<Pair<String , String>> = Paper.book().read(priorityCategory)!!
        //Toast.makeText(requireActivity(), "" + newHighIdList, Toast.LENGTH_SHORT).show()

        val fetchedLimit = MutableLiveData<Int>()
        fetchedLimit.value = 0

        //NEWLIST  {<JUDGE , {<123 , 0.2> , <145 ,0.2>)>}

        var newList : ArrayList<Pair<String,ArrayList<Pair<String,Double>>>> = arrayListOf()

        for(id in newHighIdList){
            Utils.fetchCaseDetailsFromCaseId(id.first){
                val pair = Pair(it.caseId!!, it.priorityNumber)
                val judgeId = id.second

                var found = 0


                for (outerPair in newList) {
                    val outerKey = outerPair.first
                    val innerPairs = outerPair.second

                    if (outerKey == judgeId) {
                        // Add the newInnerPair to the ArrayList of innerPairs
                        innerPairs.add(pair)

                        found = 1

                        val updatedOuterPair = Pair(outerKey, innerPairs)
                        val index = newList.indexOf(outerPair)
                        if (index != -1) {
                            newList[index] = updatedOuterPair
                        }
                        // Optionally, break out of the loop if you only want to add to the first matching outer pair
                         break
                    }
                }
                if (found == 0){
                    newList.add(Pair(id.second , arrayListOf(pair)))
                }



                fetchedLimit.value = fetchedLimit.value!! + 1
            }
        }

        fetchedLimit.observe(viewLifecycleOwner){
            if(fetchedLimit.value == newHighIdList.size){ //data fetched complete. so proceed further


                for (judges in newList){
                    val judge  = judges.first
                    val cases = judges.second
                    cases.sortBy { it.second }

                    val updatedOuterPair = Pair(judge, cases)
                    val index = newList.indexOf(judges)
                    if (index != -1) {
                        newList[index] = updatedOuterPair
                    }

                }

                Toast.makeText(requireActivity(), "$newList", Toast.LENGTH_SHORT).show()
                Log.d("nailist" , "$newList")

                for (case in newList){

                    //case.first reperesents judgeID
                    scheduleToNextAvailableDate(priorityCategory, case.first, case.second , case.second.size-1)
                }

            }
        }

    }

    private fun scheduleToNextAvailableDate(priorityCategory: String, judgeId: String, caseList : ArrayList<Pair<String,Double>>, noOfCases : Int) {

//        Log.d("konsecasekliechla" , caseId)
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
                    scheduleToNextAvailableDate(priorityCategory , judgeId , caseList , noOfCases -1)
                }

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })


    }
}