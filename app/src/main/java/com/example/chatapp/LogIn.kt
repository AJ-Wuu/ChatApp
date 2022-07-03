package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var buttonLogIn: Button
    private lateinit var buttonSignUp: Button
    private lateinit var myAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        supportActionBar?.hide()

        myAuth = FirebaseAuth.getInstance()

        editEmail = findViewById<EditText>(R.id.edit_email)
        editPassword = findViewById<EditText>(R.id.edit_password)
        buttonLogIn = findViewById<Button>(R.id.button_login)
        buttonSignUp = findViewById<Button>(R.id.button_signup)

        buttonSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        buttonLogIn.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            login(email, password)
        }
    }

    private fun login(email: String, password: String) {
        myAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@LogIn, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@LogIn, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}