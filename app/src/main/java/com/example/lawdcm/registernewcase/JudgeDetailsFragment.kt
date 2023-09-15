package com.example.lawdcm.registernewcase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.lawdcm.R
import com.example.lawdcm.databinding.FragmentJudgeDetailsBinding
import com.example.lawdcm.models.JudgeDetails
import com.example.lawdcm.singleton.ActiveJudges
import com.example.lawdcm.viewmodels.RegisterNewCaseViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import kotlin.collections.ArrayList

class JudgeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentJudgeDetailsBinding
    private lateinit var vmRegisterNewCaseViewModel: RegisterNewCaseViewModel
    private var judgeNameList : ArrayList<String> = ArrayList()
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference()
    

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJudgeDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isAdded) return

        val alljudges  = ActiveJudges.activeJudgesList.value
        judgeNameList.clear()
        for(judge in alljudges!!){
            judgeNameList.add(judge.judgeName!! + "(${judge.judgeId})")
        }
        val adapterJudgeName = ArrayAdapter(requireActivity(), R.layout.auto_textview , judgeNameList)
        binding.spinnerJudge.adapter = adapterJudgeName
        
        


        
        
        
        

        binding.btnAddToCasePool.setOnClickListener {
            var curJudge = alljudges.get(binding.spinnerJudge.selectedItemPosition)
            
            vmRegisterNewCaseViewModel.caseDetailsCollectionObject.judge = curJudge

            Toast.makeText(requireActivity(), "$curJudge", Toast.LENGTH_SHORT).show()

            var dbRefCaseDetails = dbRef.child("caseDetails").child(curJudge.courtId!!).child("newCases")
            dbRefCaseDetails.child(vmRegisterNewCaseViewModel.caseDetailsCollectionObject.caseId!!).setValue(vmRegisterNewCaseViewModel.caseDetailsCollectionObject)
                .addOnCompleteListener {
                    Toast.makeText(requireActivity(), "Case Uploaded", Toast.LENGTH_SHORT).show()
                    updateJudgeAssignedCaseIds(curJudge , vmRegisterNewCaseViewModel.caseDetailsCollectionObject.caseId!!)
                }.addOnFailureListener {
                    Toast.makeText(requireActivity(), "Try Again Later", Toast.LENGTH_SHORT).show()
                    return@addOnFailureListener

                }




//            yaha abhi judge k list of cases me caseif dalni hai


        }
    }

    private fun updateJudgeAssignedCaseIds(curJudge: JudgeDetails, caseId: String) {
        curJudge.assignedCases.add(caseId)
        var dbRefJudgeDetail = dbRef.child("judges").child(curJudge.judgeId!!)
        dbRefJudgeDetail.setValue(curJudge)
    }
}