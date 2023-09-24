package com.example.lawdcm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lawdcm.bottomnav.AllCasesFragment
import com.example.lawdcm.bottomnav.DashBoardFragment
import com.example.lawdcm.bottomnav.JudgeFragment
import com.example.lawdcm.bottomnav.ProfileFragment
import com.example.lawdcm.databinding.ActivityMainBinding
import com.example.lawdcm.singleton.registrarLoggedIn
import com.example.lawdcm.viewmodels.AllCasesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vmAllCases = ViewModelProvider(this)[AllCasesViewModel::class.java]

        registrarLoggedIn.dataFetchComplete.observe(this){ bool ->
            if(bool) {
                Utils.populateCasesIntoViewModel(registrarLoggedIn.courtId, "NEW_HIGH") {
                    vmAllCases.listNewHighPriority.postValue(it)
                }
                Utils.populateCasesIntoViewModel(registrarLoggedIn.courtId, "NEW_LOW") {
                    vmAllCases.listNewLowPriority.postValue(it)
                }
                Utils.populateCasesIntoViewModel(registrarLoggedIn.courtId, "ONGOING_HIGH") {
                    vmAllCases.listOngoingHighPriority.postValue(it)
                }
                Utils.populateCasesIntoViewModel(registrarLoggedIn.courtId, "ONGOING_LOW") {
                    vmAllCases.listOngoingLowPriority.postValue(it)
                }
                Utils.populateCasesIntoViewModel(registrarLoggedIn.courtId, "OLD") {
                    vmAllCases.listOldCases.postValue(it)
                }
                Utils.populateCasesIntoViewModel(registrarLoggedIn.courtId, "BUFFER") {
                    vmAllCases.listBuffer.postValue(it)
                }

            }
        }


        val dashboardFragment = DashBoardFragment()
        val judgeFragment = JudgeFragment()
        val allCasesFragment = AllCasesFragment()
        val profileFragment = ProfileFragment()

        val navView: BottomNavigationView = binding.navViewBottom


        setFragment(dashboardFragment)

        var idClicked = 0
        binding.imgHome.setOnClickListener {
            if (registrarLoggedIn.dataFetchComplete.value == true){
                setFragment(dashboardFragment)
                idClicked = 0
            }else{
                idClicked = binding.imgHome.id
            }
        }
        binding.imgAllJudges.setOnClickListener {
            if (registrarLoggedIn.dataFetchComplete.value == true){
                setFragment(judgeFragment)
                idClicked = 0
            }else{
                idClicked = binding.imgAllJudges.id
            }
        }
        binding.imgAddCase.setOnClickListener {
            if (registrarLoggedIn.dataFetchComplete.value == true){
                startActivity(Intent(this@MainActivity , RegisterNewCaseActivity::class.java))
                idClicked = 0
            }else{
                idClicked = binding.imgAddCase.id
            }
        }
        binding.imgCases.setOnClickListener {
            if (registrarLoggedIn.dataFetchComplete.value == true){
                setFragment(allCasesFragment)
                idClicked = 0
            }else{
                idClicked = binding.imgCases.id
            }
        }
        binding.imgProfile.setOnClickListener {
            if (registrarLoggedIn.dataFetchComplete.value == true){
                setFragment(profileFragment)
                idClicked = 0
            }else{
                idClicked = binding.imgProfile.id
            }
        }

        registrarLoggedIn.dataFetchComplete.observe(this){
            if (it){
                when(idClicked){
                    0 -> return@observe
                    R.id.imgHome -> setFragment(dashboardFragment)
                    R.id.imgAllJudges -> setFragment(judgeFragment)
                    R.id.imgAddCase -> startActivity(Intent(this@MainActivity , RegisterNewCaseActivity::class.java))
                    R.id.imgCases -> setFragment(allCasesFragment)
                    R.id.imgProfile -> setFragment(profileFragment)
                }
                idClicked = 0               //logic samaj nhi aaya 0 karne ka
            }
        }


//        navView.setOnItemSelectedListener {
//            when(it.itemId) {
//                R.id.menu_item_judges -> {
//                    registrarLoggedIn.dataFetchComplete.observe(this){b->
//                        //if(b && navView.selectedItemId == R.id.menu_item_judges) setFragment(judgeFragment)
//                    }
//                    setFragment(judgeFragment)
//                }
//                R.id.menu_item_register_new_case -> {
//                    registrarLoggedIn.dataFetchComplete.observe(this){b->
//                        if(b && navView.selectedItemId == R.id.menu_item_register_new_case ) startActivity(Intent(this@MainActivity, RegisterNewCaseActivity::class.java))
//                    }
//                }
//                R.id.menu_item_allCases -> {
//                    registrarLoggedIn.dataFetchComplete.observe(this){b->
//                        //if(b && navView.selectedItemId == R.id.menu_item_allCases) setFragment(allCasesFragment)
//                    }
//                    setFragment(allCasesFragment)
//                }
//            }
//            true
//        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_holder, fragment)
            commit()
        }
    }
}