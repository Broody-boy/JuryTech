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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.lawdcm.R
import com.example.lawdcm.Utils
import com.example.lawdcm.databinding.FragmentJudgeDetailsBinding
import com.example.lawdcm.retrofit.ApiResponse
import com.example.lawdcm.retrofit.BasePriorityNumberInterface
import com.example.lawdcm.retrofit.RetrofitClientInstance
import com.example.lawdcm.singleton.ActiveJudges
import com.example.lawdcm.singleton.registrarLoggedIn
import com.example.lawdcm.viewmodels.CaseDetailsVM
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.paperdb.Paper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlin.collections.ArrayList

class JudgeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentJudgeDetailsBinding
    private lateinit var vmCaseDetails: CaseDetailsVM
    private var judgeNameList : ArrayList<String> = ArrayList()
    private val dbRef : DatabaseReference = FirebaseDatabase.getInstance().getReference()
//    private var navController: NavController = findNavController()
    

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

//        binding.imgBack.setOnClickListener {
//            navController.popBackStack()
//        }

        val alljudges  = ActiveJudges.activeJudgesList.value
        judgeNameList.clear()
        for(judge in alljudges!!){
            judgeNameList.add(judge.judgeName!! + "(${judge.judgeId})")
        }
        val adapterJudgeName = ArrayAdapter(requireActivity(), R.layout.auto_textview , judgeNameList)
        binding.spinnerJudge.adapter = adapterJudgeName

        vmCaseDetails = ViewModelProvider(requireActivity())[CaseDetailsVM::class.java]

        binding.btnAddToPaperQueue.setOnClickListener {
            getCasePriority()

        }
    }
    private fun getCasePriority() {

        val api = RetrofitClientInstance.getClient().create(BasePriorityNumberInterface::class.java)
        val call = api.getBasePriority("13" , "46" , vmCaseDetails.caseDetailsCollectionObject.caseCategory!!,"75")
        call.enqueue(object : Callback<ApiResponse>{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful){
                    val result = response.body()
                    Toast.makeText(requireActivity(), "${result!!.daysGroup} + ${result!!.lagGroup}", Toast.LENGTH_SHORT).show()

                    val caseId = vmCaseDetails.caseDetailsCollectionObject.caseId!!
                    val caseType = vmCaseDetails.caseDetailsCollectionObject.caseType
                    val importance = vmCaseDetails.caseDetailsCollectionObject.matterType
                    val age = vmCaseDetails.caseDetailsCollectionObject.dateOfFiling
                    val judgeId = vmCaseDetails.caseDetailsCollectionObject.judgeId

                    var finalPriorityNumber = Utils.getFinalPriority(result.daysGroup!! , result.lagGroup!! , caseType!! , importance!! , age!!)

                    var priorityCategory = if(finalPriorityNumber!! > 1.5){ "NEW_HIGH" }else{ "NEW_LOW" }

                    vmCaseDetails.caseDetailsCollectionObject.priorityCategory = priorityCategory
                    vmCaseDetails.caseDetailsCollectionObject.priorityNumber = finalPriorityNumber!!

                    //Toast.makeText(requireActivity(), "Final priority $priority", Toast.LENGTH_SHORT).show()
                    //Toast.makeText(requireActivity(), "Final priority ${vmRegisterNewCaseViewModel.caseDetailsCollectionObject.priorityCategory}", Toast.LENGTH_SHORT).show()

                    val alljudges  = ActiveJudges.activeJudgesList.value
                    var curJudge = alljudges!!.get(binding.spinnerJudge.selectedItemPosition)

                    uploadDetailsIntoFirebase()
                    addToPaperQueue(caseId, priorityCategory , curJudge.judgeId!!)
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

    private fun addToPaperQueue(caseId : String, priorityCategory: String , judgeId : String) {

        Toast.makeText(requireActivity(), judgeId, Toast.LENGTH_SHORT).show()
        Log.d("judgu" , judgeId)

//                                         judgeId           caseId
        var judgeWiseCasesHashMap : HashMap<String, ArrayList<String>>
        judgeWiseCasesHashMap = Paper.book().read(priorityCategory) ?: hashMapOf()

        var assignedList = judgeWiseCasesHashMap.getOrDefault(judgeId , arrayListOf())
        assignedList.add(caseId)
        judgeWiseCasesHashMap.set(judgeId, assignedList)
        Paper.book().write(priorityCategory, judgeWiseCasesHashMap)


//        var savedList : ArrayList<Pair<String , String>> = Paper.book().read(priorityCategory) ?: arrayListOf()
//        savedList.add(Pair(caseId , judgeId))
//        Paper.book().write(priorityCategory, savedList)
    }


    private fun uploadDetailsIntoFirebase() {

        val alljudges  = ActiveJudges.activeJudgesList.value
        var curJudge = alljudges!!.get(binding.spinnerJudge.selectedItemPosition)

        vmCaseDetails.caseDetailsCollectionObject.judgeId = curJudge.judgeId
        vmCaseDetails.caseDetailsCollectionObject.judgeName = curJudge.judgeName

        //Toast.makeText(requireActivity(), "$curJudge", Toast.LENGTH_SHORT).show()

        dbRef.child("caseDetails").child(registrarLoggedIn.courtId)
            .child(vmCaseDetails.caseDetailsCollectionObject.caseId!!).setValue(vmCaseDetails.caseDetailsCollectionObject)
            .addOnCompleteListener {
                Toast.makeText(requireActivity(), "Case Uploaded", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }.addOnFailureListener {
                Toast.makeText(requireActivity(), "Try Again Later", Toast.LENGTH_SHORT).show()
                return@addOnFailureListener
            }
    }

//    private fun addCaseForJudge() {
//
//        Toast.makeText(requireActivity(), "reched addCAsesjudge", Toast.LENGTH_SHORT).show()
//        val judgeId: String? = vmRegisterNewCaseViewModel.caseDetailsCollectionObject.judgeId
//        val caseId: String? = vmRegisterNewCaseViewModel.caseDetailsCollectionObject.caseId
//        val priorityNumber: Double? = vmRegisterNewCaseViewModel.caseDetailsCollectionObject.priorityNumber
//        val priorityCategory: String? = vmRegisterNewCaseViewModel.caseDetailsCollectionObject.priorityCategory
//
//
//
//        var caseList  : ArrayList<String> = arrayListOf()
//
//        val dbQueryPath = dbRef.child("casePool").child(registrarLoggedIn.courtId!!).child(priorityCategory!!)
//
//        dbQueryPath.addListenerForSingleValueEvent(object  : ValueEventListener{
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                Toast.makeText(requireActivity(), "ondatachanged", Toast.LENGTH_SHORT).show()
//                if (snapshot.exists()){
//                    caseList = snapshot.value as ArrayList<String>
//                }
//                caseList.add(caseId!!)
//
//                Toast.makeText(requireActivity(), "after ondata", Toast.LENGTH_SHORT).show()
//                dbQueryPath.setValue(caseList)
//                    .addOnCompleteListener {
//                        Toast.makeText(requireActivity(), "Case Added to Judge", Toast.LENGTH_SHORT).show()
//                    }.addOnFailureListener {
//                        Toast.makeText(requireActivity(), "Try Again Later", Toast.LENGTH_SHORT).show()
//                        return@addOnFailureListener
//                    }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })
//
//
//    }

   /* private fun updateJudgeAssignedCaseIds(curJudge: JudgeDetails, caseId: String) {
        curJudge.assignedCases.add(caseId)
        var dbRefJudgeDetail = dbRef.child("judges").child(curJudge.judgeId!!)
        dbRefJudgeDetail.setValue(curJudge)
    }*/
}