package com.example.lawdcm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoginPage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("registrars")

        if(auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val registrarId = binding.etRegistrarId;
        val officialEmail = binding.etEmail;
        val passKey = binding.etPassKEY;

        binding.btnLogin.setOnClickListener {

        }
    }
}