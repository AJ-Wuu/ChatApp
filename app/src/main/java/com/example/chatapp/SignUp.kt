package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var editUsername: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var myAuth: FirebaseAuth
    private lateinit var myDatabaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        myAuth = FirebaseAuth.getInstance()

        editUsername = findViewById<EditText>(R.id.edit_username)
        editEmail = findViewById<EditText>(R.id.edit_email)
        editPassword = findViewById<EditText>(R.id.edit_password)
        buttonSignUp = findViewById<Button>(R.id.button_signup)

        buttonSignUp.setOnClickListener {
            val name = editUsername.text.toString()
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            signup(name, email, password)
        }
    }

    private fun signup(name: String, email: String, password: String) {
        myAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, myAuth.currentUser?.uid!!)

                    val intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else {
                    Toast.makeText(this@SignUp, "Some error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        myDatabaseRef = FirebaseDatabase.getInstance().getReference()
        myDatabaseRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}