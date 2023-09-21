package com.example.lawdcm.registernewcase

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.lawdcm.R
import com.example.lawdcm.Utils
import com.example.lawdcm.databinding.FragmentJudgeDetailsBinding
import com.example.lawdcm.models.JudgeDetails
import com.example.lawdcm.retrofit.ApiResponse
import com.example.lawdcm.retrofit.BasePriorityNumberInterface
import com.example.lawdcm.retrofit.RetrofitClientInstance
import com.example.lawdcm.singleton.ActiveJudges
import com.example.lawdcm.viewmodels.RegisterNewCaseViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create

import kotlin.collections.ArrayList

class JudgeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentJudgeDetailsBinding
    private lateinit var vmRegisterNewCaseViewModel: RegisterNewCaseViewModel
    private var judgeNameList : ArrayList<String> = ArrayList()
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference()
    private var priority : Double? = null
    

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

        vmRegisterNewCaseViewModel = ViewModelProvider(requireActivity())[RegisterNewCaseViewModel::class.java]


        
        
        
        

        binding.btnAddToCasePool.setOnClickListener {

            getCasePriority()
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

    private fun getCasePriority() {
        val api = RetrofitClientInstance.getClient().create(BasePriorityNumberInterface::class.java)
        val call = api.getBasePriority("13" , "46" , "5979" ,"75")
        call.enqueue(object : Callback<ApiResponse>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful){
                    val result = response.body()
                    Toast.makeText(requireActivity(), "${result!!.daysGroup} + ${result!!.lagGroup}", Toast.LENGTH_SHORT).show()

                    val caseType = vmRegisterNewCaseViewModel.caseDetailsCollectionObject.caseType
                    val importance = vmRegisterNewCaseViewModel.caseDetailsCollectionObject.matterType
                    val age = vmRegisterNewCaseViewModel.caseDetailsCollectionObject.dateOfFiling
                    priority = Utils.getFinalPriority(result.daysGroup!! , result.lagGroup!! , caseType!! , importance!! , age!!)

                    if(priority!! > 1.5){
                        vmRegisterNewCaseViewModel.caseDetailsCollectionObject.priorityCategory = "NEW_HIGH"
                    }else{
                        vmRegisterNewCaseViewModel.caseDetailsCollectionObject.priorityCategory = "NEW_LOW"
                    }
                    vmRegisterNewCaseViewModel.caseDetailsCollectionObject.priorityNumber = priority!!

                    Toast.makeText(requireActivity(), "Final priority $priority", Toast.LENGTH_SHORT).show()
                    Toast.makeText(requireActivity(), "Final priority ${vmRegisterNewCaseViewModel.caseDetailsCollectionObject.priorityCategory}", Toast.LENGTH_SHORT).show()
                    Log.d("response1" ,"Final priority $priority")
                    Log.d("response1" ,"Final priority ${vmRegisterNewCaseViewModel.caseDetailsCollectionObject.priorityCategory}")
                    Log.d("response" ,"${result!!.daysGroup} + ${result!!.lagGroup}" )
                }else{
                    Toast.makeText(requireActivity(), "Response Invalid", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(requireActivity(), t.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("error" ,t.localizedMessage )
            }

        })
        Toast.makeText(requireActivity(), "inside the function", Toast.LENGTH_SHORT).show()
    }

    private fun updateJudgeAssignedCaseIds(curJudge: JudgeDetails, caseId: String) {
        curJudge.assignedCases.add(caseId)
        var dbRefJudgeDetail = dbRef.child("judges").child(curJudge.judgeId!!)
        dbRefJudgeDetail.setValue(curJudge)
    }
}