package com.example.lawdcm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.lawdcm.bottomnav.AllCasesFragment
import com.example.lawdcm.bottomnav.JudgeFragment
import com.example.lawdcm.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val judgeFragment = JudgeFragment()
        val allCasesFragment = AllCasesFragment()

        val navView: BottomNavigationView = binding.navViewBottom

        navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_item_judges -> {
                    setFragment(judgeFragment)
                }

                R.id.menu_item_register_new_case -> {
                    startActivity(Intent(this@MainActivity, RegisterNewCaseActivity::class.java))
                }

                R.id.menu_item_allCases -> {
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