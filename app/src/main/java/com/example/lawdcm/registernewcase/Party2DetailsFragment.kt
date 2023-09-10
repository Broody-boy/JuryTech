package com.example.lawdcm.registernewcase

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
import com.example.lawdcm.databinding.FragmentParty2DetailsBinding
import com.example.lawdcm.models.PartyDetails
import com.example.lawdcm.viewmodels.RegisterNewCaseViewModel

class Party2DetailsFragment : Fragment() {

    private lateinit var binding: FragmentParty2DetailsBinding
    private lateinit var navController: NavController
    private lateinit var vmRegisterNewCaseViewModel: RegisterNewCaseViewModel
    private var respondentDetails = PartyDetails()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParty2DetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isAdded) return

        vmRegisterNewCaseViewModel = ViewModelProvider(requireActivity())[RegisterNewCaseViewModel::class.java]

        navController = findNavController()

        binding.btnNext.setOnClickListener {
            updatePartyDetailsIntoViewModel()
            navController.navigate(R.id.action_party2DetailsFragment_to_judgeDetailsFragment)
        }
    }
    private fun updatePartyDetailsIntoViewModel() {
        respondentDetails.name = binding.etNameOfRespondent.text.toString()
        respondentDetails.dateOfBirth = binding.tvTapToChooseDateOfBirth.text.toString()
        respondentDetails.gender = binding.rgGender.findViewById<RadioButton>(binding.rgGender
            .checkedRadioButtonId).text.toString()
        respondentDetails.relativeName = binding.etRelativeName.text.toString()
        respondentDetails.relativeRelation = binding.etRelativeRelation.text.toString()
        /*respondentDetails.religion = binding.etReligion.text.toString()
        respondentDetails.caste = binding.etCaste.text.toString()
        respondentDetails.contact.emailId = binding.etEmailId.text.toString()
        respondentDetails.contact.phoneNumber = binding.etPhoneNumber.text.toString()
        respondentDetails.address.address = binding.etCompleteAddress.text.toString()
        respondentDetails.address.state = binding.etState.text.toString()
        respondentDetails.address.district = binding.etDistrict.text.toString()
        respondentDetails.address.taluka = binding.etTaluka.text.toString()
        respondentDetails.address.pinCode = binding.etPinCode.text.toString()*/
        respondentDetails.associatedCaseId = vmRegisterNewCaseViewModel.caseDetailsCollectionObject.caseId

        vmRegisterNewCaseViewModel.respondentDetails = respondentDetails
        vmRegisterNewCaseViewModel.caseDetailsCollectionObject.respondentId = respondentDetails
            .contact.phoneNumber

    }
}