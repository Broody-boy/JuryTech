package com.example.lawdcm.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lawdcm.R
import com.example.lawdcm.Utils
import com.example.lawdcm.adapters.AllCasesAdapter
import com.example.lawdcm.databinding.ActivityScheduledCasesBinding
import com.example.lawdcm.viewmodels.DashBoardViewModel
import com.google.android.material.chip.Chip

class ScheduledCases : AppCompatActivity() {

    private lateinit var binding : ActivityScheduledCasesBinding
    private lateinit var vm : DashBoardViewModel
    private lateinit var judgeId : String
    private lateinit var adapter : AllCasesAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduledCasesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(this)[DashBoardViewModel::class.java]
        judgeId = intent.getStringExtra("judgeId")!!
        populateCases(){

            adapter.setCaseList(vm.listNewHighPriority.value!!)

            for (i in 0 until binding.cgScCaseCategory.childCount) {
                val chip = binding.cgScCaseCategory.getChildAt(i) as Chip
                chip.setOnClickListener {
                    changecases(chip.text.toString())
                    Toast.makeText(this, chip.text.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        adapter = AllCasesAdapter(this)
        binding.rvScheduledCases.adapter = adapter
        binding.rvScheduledCases.layoutManager = LinearLayoutManager(this)

    }

    private fun changecases(casePriority: String) {

        when (casePriority){
            "New High" -> adapter.setCaseList(vm.listNewHighPriority.value!!)
            "New Low" -> adapter.setCaseList(vm.listNewLowPriority.value!!)
            "Ongoing High" -> adapter.setCaseList(vm.listOngoingHighPriority.value!!)
            "Ongoing Low" -> adapter.setCaseList(vm.listOngoingLowPriority.value!!)
            "Old" -> adapter.setCaseList(vm.listOldCases.value!!)
            "Buffer" -> adapter.setCaseList(vm.listBuffer.value!!)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun populateCases(callback: () -> Unit) {

        var i = 0
        vm.populateTodayCasesPriorityWise(judgeId!! , "NEW_HIGH"){caseIds ->
            Utils.makeArrayListofCaseDetailsFromArrayListOfCaseIds(caseIds){
                vm.listNewHighPriority.postValue(it)
                i++
                if(i==6){
                    callback()
                }
            }
        }
        vm.populateTodayCasesPriorityWise(judgeId!! , "NEW_LOW"){caseIds ->
            Utils.makeArrayListofCaseDetailsFromArrayListOfCaseIds(caseIds){
                vm.listNewLowPriority.postValue(it)
                i++
                if(i==6){
                    callback()
                }
            }
        }
        vm.populateTodayCasesPriorityWise(judgeId!! , "ONGOING_HIGH"){caseIds ->
            Utils.makeArrayListofCaseDetailsFromArrayListOfCaseIds(caseIds){
                vm.listOngoingHighPriority.postValue(it)
                i++
                if(i==6){
                    callback()
                }
            }
        }
        vm.populateTodayCasesPriorityWise(judgeId!! , "ONGOING_LOW"){caseIds ->
            Utils.makeArrayListofCaseDetailsFromArrayListOfCaseIds(caseIds){
                vm.listOngoingLowPriority.postValue(it)
                i++
                if(i==6){
                    callback()
                }
            }
        }
        vm.populateTodayCasesPriorityWise(judgeId!! , "OLD"){caseIds ->
            Utils.makeArrayListofCaseDetailsFromArrayListOfCaseIds(caseIds){
                vm.listOldCases.postValue(it)
                i++
                if(i==6){
                    callback()
                }
            }
        }
        vm.populateTodayCasesPriorityWise(judgeId!! , "BUFFER"){caseIds ->
            Utils.makeArrayListofCaseDetailsFromArrayListOfCaseIds(caseIds){
                vm.listBuffer.postValue(it)
                i++
                if(i==6){
                    callback()
                }
            }
        }
    }
}