package com.example.lawdcm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.example.lawdcm.databinding.ActivityLoginPageBinding
import com.example.lawdcm.models.RegistrarAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginPage : AppCompatActivity() {
    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var auth: FirebaseAuth
    val dbreference = FirebaseDatabase.getInstance().getReference("registrars")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val registrarId = binding.etRegistrarId;
        val officialEmail = binding.etEmail;
        val passKey = binding.etPassKEY;

        binding.btnLogin.setOnClickListener {
            val courtType = findViewById<RadioButton>(binding.rgCourtType.checkedRadioButtonId)
                .text.toString().uppercase()
            val dbquery = dbreference.child(registrarId.text.toString())
            dbquery.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){

                        val data = snapshot.getValue(RegistrarAuth::class.java)

                        if (data != null) {
                            if(officialEmail.text.toString() == data.officialEmail &&
                                passKey.text.toString() == data.passKey
                                && courtType == data.courtType){
                                startActivity(Intent(this@LoginPage , MainActivity::class.java))
                                finish()
                            }
                            else{
                                Toast.makeText(this@LoginPage, "Wrong Credentials",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(this@LoginPage, "Registrar Id not valid",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
            auth.signInWithEmailAndPassword(officialEmail.text.toString(), passKey.text.toString())
        }
    }
}