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
import com.example.lawdcm.viewmodels.RegisterNewCaseViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val vmAllCases = ViewModelProvider(this)[AllCasesViewModel::class.java]

        registrarLoggedIn.dataFetchComplete.observe(this){ b ->
            if(b)
                Utils.populateCasesIntoViewModel(registrarLoggedIn.courtId){
                    vmAllCases.listOfAllCases.postValue(it)
                }
        }


        val dashboardFragment = DashBoardFragment()
        val judgeFragment = JudgeFragment()
        val allCasesFragment = AllCasesFragment()
        val profileFragment = ProfileFragment()

        val navView: BottomNavigationView = binding.navViewBottom


        setFragment(profileFragment)


        navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_item_judges -> {
                    registrarLoggedIn.dataFetchComplete.observe(this){b->
                        //if(b && navView.selectedItemId == R.id.menu_item_judges) setFragment(judgeFragment)
                    }
                    setFragment(judgeFragment)
                }
                R.id.menu_item_register_new_case -> {
                    registrarLoggedIn.dataFetchComplete.observe(this){b->
                        if(b && navView.selectedItemId == R.id.menu_item_register_new_case ) startActivity(Intent(this@MainActivity, RegisterNewCaseActivity::class.java))
                    }
                }
                R.id.menu_item_allCases -> {
                    registrarLoggedIn.dataFetchComplete.observe(this){b->
                        //if(b && navView.selectedItemId == R.id.menu_item_allCases) setFragment(allCasesFragment)
                    }
                    setFragment(allCasesFragment)
                }
            }
            true
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_holder, fragment)
            commit()
        }
    }
}