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
import com.example.lawdcm.databinding.FragmentParty1DetailsBinding
import com.example.lawdcm.models.PartyDetails
import com.example.lawdcm.viewmodels.CaseDetailsVM
import java.util.Calendar

class Party1DetailsFragment : Fragment() {

    private lateinit var binding: FragmentParty1DetailsBinding
    private lateinit var navController: NavController
    private lateinit var vmRegisterNewCaseViewModel: CaseDetailsVM
    private var applicantDetails = PartyDetails()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParty1DetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isAdded) return

        vmRegisterNewCaseViewModel = ViewModelProvider(requireActivity())[CaseDetailsVM::class.java]

        navController = findNavController()

        binding.tvTapToChooseDateOfBirth.setOnClickListener {
            val c = Calendar.getInstance()

            val datePickerDialog = DatePickerDialog(requireActivity(),{view, year, monthOfYear, dayOfMonth ->
                val selectedYear = "${year}"
                var selectedMonth = "${monthOfYear + 1}"
                var selectedDay = "$dayOfMonth"

                if((monthOfYear + 1) < 10) {   selectedMonth = "0" + selectedMonth }
                if(dayOfMonth < 10) {   selectedDay = "0" + selectedDay }
                binding.tvTapToChooseDateOfBirth.text = "${selectedDay}-${selectedMonth}-${selectedYear}"
                applicantDetails.dateOfBirth= binding.tvTapToChooseDateOfBirth.text.toString()
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()
        }

        binding.btnNext.setOnClickListener {
            updatePartyDetailsIntoViewModel()
            navController.navigate(R.id.action_party1DetailsFragment_to_party2DetailsFragment)
        }
    }

    private fun updatePartyDetailsIntoViewModel() {
        applicantDetails.name = binding.etNameOfApplicant.text.toString()
        applicantDetails.dateOfBirth = binding.tvTapToChooseDateOfBirth.text.toString()
        applicantDetails.gender = binding.rgGender.findViewById<RadioButton>(binding.rgGender
            .checkedRadioButtonId).text.toString()
        applicantDetails.relativeName = binding.etRelativeName.text.toString()
        applicantDetails.relativeRelation = binding.etRelativeRelation.text.toString()
        applicantDetails.religion = binding.etReligion.text.toString()
        applicantDetails.caste = binding.etCaste.text.toString()
        applicantDetails.contact.emailId = binding.etEmailId.text.toString()
        applicantDetails.contact.phoneNumber = binding.etPhoneNumber.text.toString()
        applicantDetails.address.address = binding.etCompleteAddress.text.toString()
        applicantDetails.address.state = binding.etState.text.toString()
        applicantDetails.address.district = binding.etDistrict.text.toString()
        applicantDetails.address.taluka = binding.etTaluka.text.toString()
        applicantDetails.address.pinCode = binding.etPinCode.text.toString()
        applicantDetails.associatedCaseId = vmRegisterNewCaseViewModel.caseDetailsCollectionObject.caseId

        vmRegisterNewCaseViewModel.applicantDetails = applicantDetails
        vmRegisterNewCaseViewModel.caseDetailsCollectionObject.applicantId = applicantDetails
            .contact.phoneNumber

    }
}