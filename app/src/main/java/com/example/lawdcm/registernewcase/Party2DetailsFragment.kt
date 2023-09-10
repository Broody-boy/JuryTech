package com.example.lawdcm.registernewcase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.lawdcm.R
import com.example.lawdcm.databinding.FragmentParty2DetailsBinding

class Party2DetailsFragment : Fragment() {

    private lateinit var binding: FragmentParty2DetailsBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentParty2DetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.btnNext.setOnClickListener {
            navController.navigate(R.id.action_party2DetailsFragment_to_judgeDetailsFragment)
        }
    }
}