package com.aqeel.adminapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.aqeel.adminapp.databinding.ActivitySignUpBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.SignUpBtnId.setOnClickListener{

            val mail= binding.EmailBox.text.toString()
            val pass= binding.passwordBox.text.toString()
            val cPass= binding.CPasswordBox.text.toString()
            if (pass == cPass){
                FirebaseMessaging.getInstance().token.addOnCompleteListener{task->
                    if (task.isSuccessful){
                        val token= task.result
                        val adminModel= AdminModel(mail,pass,token)
                        saveAdminToFireStore(adminModel)
                    }
                }
            }else{
                Toast.makeText(this,"Pass not match", Toast.LENGTH_SHORT).show()
            }
        }
        binding.LoginBtnId.setOnClickListener{
            startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
            finish()
        }

    }
    private fun saveAdminToFireStore(adminModel: AdminModel) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Admin")
            .document(adminModel.email)
            .set(adminModel)
            .addOnSuccessListener {
                Log.d("Firestore", "User data saved with device token")
                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                finish()
            }.addOnFailureListener{

            }
    }
}