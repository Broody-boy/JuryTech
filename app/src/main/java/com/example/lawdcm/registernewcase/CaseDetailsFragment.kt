package com.example.lawdcm.registernewcase

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.lawdcm.R
import com.example.lawdcm.databinding.FragmentCaseDetailsBinding
import com.example.lawdcm.models.CaseDetails
import com.example.lawdcm.singleton.ActiveJudges
import com.example.lawdcm.viewmodels.RegisterNewCaseViewModel
import java.util.Calendar

class CaseDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCaseDetailsBinding
    private lateinit var navController: NavController
    private lateinit var vmRegisterNewCaseViewModel: RegisterNewCaseViewModel
    private var currObj = CaseDetails()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCaseDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isAdded) return

        ActiveJudges.getJudgesListFromFirebase("28398")

        vmRegisterNewCaseViewModel = ViewModelProvider(requireActivity())[RegisterNewCaseViewModel::class.java]

        binding.tvTapToChooseDateOfFilling.setOnClickListener {
            val c = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(requireActivity(),{view, year, monthOfYear, dayOfMonth ->
                val selectedYear = "${year}"
                var selectedMonth = "${monthOfYear + 1}"
                var selectedDay = "$dayOfMonth"

                if((monthOfYear + 1) < 10) {   selectedMonth = "0" + selectedMonth }
                if(dayOfMonth < 10) {   selectedDay = "0" + selectedDay }
                binding.tvTapToChooseDateOfFilling.text = "${selectedDay}-${selectedMonth}-${selectedYear}"
                currObj.dateOfFiling = binding.tvTapToChooseDateOfFilling.text.toString()
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        binding.rgMatterType.setOnCheckedChangeListener { group, checkedId ->
            currObj.matterType = group.findViewById<RadioButton>(checkedId).text.toString().uppercase()
        }

        navController = findNavController()

        binding.btnNext.setOnClickListener {
            updateCaseDetailsIntoViewModel()
            navController.navigate(R.id.action_caseDetailsFragment_to_party1DetailsFragment)
        }
    }

    private fun updateCaseDetailsIntoViewModel() {
        currObj.caseId = binding.etCaseId.text.toString()
        //currObj.caseName = binding.etCaseName.text.toString()
        currObj.caseType = binding.etCaseType.text.toString().uppercase()
        currObj.dateOfFiling = binding.tvTapToChooseDateOfFilling.text.toString()
        currObj.caseCategory = binding.etCaseCategory.text.toString()
        currObj.matterType = binding.rgMatterType.findViewById<RadioButton>(binding.rgMatterType
            .checkedRadioButtonId).text.toString().uppercase()
        currObj.causeOfAction = binding.etCauseOfAction.text.toString()
        currObj.caseAct = binding.etCaseAct.text.toString()
        currObj.caseActSection = binding.etCaseActSection.text.toString()

        vmRegisterNewCaseViewModel.caseDetailsCollectionObject = currObj

    }
}