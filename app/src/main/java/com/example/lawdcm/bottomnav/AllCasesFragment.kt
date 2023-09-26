package com.example.lawdcm.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lawdcm.R
import com.example.lawdcm.adapters.AllCasesAdapter
import com.example.lawdcm.databinding.FragmentAllCasesBinding
import com.example.lawdcm.viewmodels.AllCasesViewModel
import com.google.android.material.chip.Chip

class AllCasesFragment : Fragment() {

    private lateinit var binding : FragmentAllCasesBinding
    private lateinit var adapter : AllCasesAdapter
    private lateinit var vmAllCasesViewModel : AllCasesViewModel

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

        vmAllCasesViewModel = ViewModelProvider(requireActivity())[AllCasesViewModel::class.java]

        vmAllCasesViewModel.listNewHighPriority.observe(viewLifecycleOwner, Observer {
            if(!isAdded ) return@Observer

            adapter.setCaseList(it)
        })






        for (i in 0 until binding.cgCaseCategory.childCount) {
            val chip = binding.cgCaseCategory.getChildAt(i) as Chip
            chip.setOnClickListener {
                changecases(chip.text.toString())
                Toast.makeText(requireActivity(), chip.text.toString(), Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun changecases(casePriority : String) {

        when (casePriority){
            "New High" ->{
                vmAllCasesViewModel.listNewHighPriority.observe(viewLifecycleOwner, Observer {
                    if(!isAdded ) return@Observer

                    adapter.setCaseList(it)
                })
            }
            "New Low" ->{
                vmAllCasesViewModel.listNewLowPriority.observe(viewLifecycleOwner, Observer {
                    if(!isAdded) return@Observer

                    adapter.setCaseList(it)
                })
            }
            "Ongoing High" ->{
                vmAllCasesViewModel.listOngoingHighPriority.observe(viewLifecycleOwner, Observer {
                    if(!isAdded) return@Observer

                    adapter.setCaseList(it)
                })
            }
            "Ongoing Low" ->{
                vmAllCasesViewModel.listOngoingLowPriority.observe(viewLifecycleOwner, Observer {
                    if(!isAdded ) return@Observer

                    adapter.setCaseList(it)
                })
            }
            "Old" ->{
                vmAllCasesViewModel.listOldCases.observe(viewLifecycleOwner, Observer {
                    if(!isAdded ) return@Observer

                    adapter.setCaseList(it)
                })
            }
            "Buffer" ->{
                vmAllCasesViewModel.listBuffer.observe(viewLifecycleOwner, Observer {
                    if(!isAdded ) return@Observer

                    adapter.setCaseList(it)
                })
            }
        }

    }
}