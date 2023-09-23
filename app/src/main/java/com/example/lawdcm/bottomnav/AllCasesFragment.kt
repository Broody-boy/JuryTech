package com.example.lawdcm.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lawdcm.adapters.AllCasesAdapter
import com.example.lawdcm.databinding.FragmentAllCasesBinding
import com.example.lawdcm.viewmodels.AllCasesViewModel

class AllCasesFragment : Fragment() {

    private lateinit var binding : FragmentAllCasesBinding
    private lateinit var adapter : AllCasesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllCasesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AllCasesAdapter(requireActivity())
        binding.rvCases.adapter = adapter

        val vmAllCasesViewModel = ViewModelProvider(requireActivity())[AllCasesViewModel::class.java]
        vmAllCasesViewModel.listOfAllCases.observe(viewLifecycleOwner, Observer {
            if(!isAdded || it.isEmpty()) return@Observer

            adapter.setCaseList(it)
        })
    }
}