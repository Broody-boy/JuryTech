package com.example.lawdcm.registernewcase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.lawdcm.databinding.FragmentJudgeDetailsBinding
import com.example.lawdcm.viewmodels.RegisterNewCaseViewModel

class JudgeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentJudgeDetailsBinding
    private lateinit var vmRegisterNewCaseViewModel: RegisterNewCaseViewModel

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

        vmRegisterNewCaseViewModel = ViewModelProvider(requireActivity())[RegisterNewCaseViewModel::class.java]

        binding.btnAddToCasePool.setOnClickListener {

        }
    }
}