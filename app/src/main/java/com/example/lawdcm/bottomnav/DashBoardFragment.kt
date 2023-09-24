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
import com.example.lawdcm.Utils
import com.example.lawdcm.databinding.FragmentDashBoardBinding
import com.example.lawdcm.singleton.registrarLoggedIn
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.paperdb.Paper

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
            scheduleCases("", "")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleCases(priorityCategory: String, judgeId : String) {

        val priorityCategoryDUMMY = "NEW_HIGH"
        val judgeIdDUMMY = "808080"

        val newHighIdList : ArrayList<String> = Paper.book().read(priorityCategoryDUMMY)!!
        //Toast.makeText(requireActivity(), "" + newHighIdList, Toast.LENGTH_SHORT).show()

        val fetchedLimit = MutableLiveData<Int>()
        fetchedLimit.value = 0

        var newList : ArrayList<Pair<String,Double>> = arrayListOf()

        for(id in newHighIdList){
            Utils.fetchCaseDetailsFromCaseId(id){
                newList.add(Pair(id, it.priorityNumber))
                fetchedLimit.value = fetchedLimit.value!! + 1
            }
        }

        fetchedLimit.observe(viewLifecycleOwner){
            if(fetchedLimit.value == newHighIdList.size){   //data fetched complete. so proceed further
                newList.sortByDescending { it.second }
                Toast.makeText(requireActivity(), "$newList", Toast.LENGTH_SHORT).show()

                for(pair in newList){
                    scheduleToNextAvailableDate(priorityCategoryDUMMY, judgeIdDUMMY, pair.first)
                }
            }
        }

        //Toast.makeText(requireActivity(), "${Utils.getCurrentDate()}", Toast.LENGTH_SHORT).show()
    }

    private fun scheduleToNextAvailableDate(priorityCategory: String, judgeId: String, caseId : String) {
        //add judge care taking afterwards
        dbRef.child("casePool").child(registrarLoggedIn.courtId).child(judgeId).child(priorityCategory).addListenerForSingleValueEvent(object: ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {

                if(!snapshot.exists()) {  //nothing there
                    Toast.makeText(requireActivity(), "switch 1", Toast.LENGTH_SHORT).show()
                    snapshot.ref.child(Utils.getCurrentDate()).setValue(arrayListOf(caseId))
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "switch 1 complete", Toast.LENGTH_SHORT).show()
                        }
                }
                else{   //some dates exist
                    var lastDate  = snapshot.children.last().key!!
                    var lastDateList  = snapshot.children.last().value as ArrayList<String>

                    if(lastDateList.size < 4){
                        lastDateList.add(caseId)
                        snapshot.ref.child(lastDate).setValue(lastDateList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}