package com.example.firebaseauthdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registerlayout)

        val btnRegister: Button = findViewById(R.id.btnRegister)
        val emailInput: TextInputEditText = findViewById(R.id.emailRegisterInput)
        val passwordInput: TextInputEditText = findViewById(R.id.passwordRegisterInput)
        val nameInput: TextInputEditText = findViewById(R.id.nameRegisterInput)

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        val auth: FirebaseAuth = FirebaseAuth.getInstance()

        val btnLogin: TextView = findViewById(R.id.loginRedirectRegister)
        btnLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        btnRegister.setOnClickListener {
            when {
                TextUtils.isEmpty(emailInput.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Por favor, insira um e-mail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(passwordInput.text.toString().trim { it <= ' '}) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Por favor, insira uma senha.",
                        Toast.LENGTH_SHORT
                    ).show()
                }TextUtils.isEmpty(nameInput.text.toString().trim { it <= ' '}) -> {
                Toast.makeText(
                    this@RegisterActivity,
                    "Por favor, insira o nome",
                    Toast.LENGTH_SHORT
                ).show()
            }
                else -> {
                    val email: String = emailInput.text.toString().trim { it <= ' '}
                    val password: String = passwordInput.text.toString().trim { it <= ' '}
                    val name: String = nameInput.text.toString().trim { it <= ' ' }

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {

                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registrado com sucesso",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val initialBalance: Double = 100.0
                                val userInfo: MutableMap<String, Any>  = HashMap()
                                userInfo["uid"] = firebaseUser.uid
                                userInfo["balance"] = initialBalance
                                userInfo["accountName"] = name
                                db.collection("users").document(firebaseUser.uid)
                                    .set(userInfo)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "User added to the database.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "User not added to the database.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                val intent =
                                    Intent(this@RegisterActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(
                                    "user_accountName",
                                    name
                                )
                                intent.putExtra(
                                    "user_balance",
                                    initialBalance
                                )
                                startActivity(intent)
                                finish()
                            } else {

                                Toast.makeText(
                                    this@RegisterActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                }
            }
        }
    }
}