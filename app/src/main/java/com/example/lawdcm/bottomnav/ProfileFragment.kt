package com.example.lawdcm.bottomnav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.lawdcm.databinding.FragmentProfileBinding
import com.example.lawdcm.singleton.registrarLoggedIn

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registrarName = registrarLoggedIn.registrarDetails.name
        val imageUrl = registrarLoggedIn.registrarDetails.imageUrl
        val registrarId = registrarLoggedIn.registrarDetails.registrarId
        val registrarCourtId = registrarLoggedIn.registrarDetails.courtId
        val registrarCourtType = registrarLoggedIn.registrarDetails.courtType

        binding.registrarName.text = "Hi, $registrarName"
        Glide.with(requireActivity()).load(imageUrl).into(binding.ivProfile)
        binding.registrarId.text = registrarId
        binding.registrarEmail.text = registrarLoggedIn.registrarDetails.officialEmail
        binding.registrarCourtId.text = registrarLoggedIn.registrarDetails.courtId
        binding.registrarCourtType.text = registrarLoggedIn.registrarDetails.courtType
    }
}