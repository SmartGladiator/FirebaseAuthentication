package com.example.firebaseauthentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText

    private lateinit var signupBtn: Button
    private lateinit var loginBtn: Button

    private lateinit var resetPasswordTv: TextView
    private val sharedPrefFile = "sharedpreference"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)


        emailEt = findViewById(R.id.email_edt_text)
        passwordEt = findViewById(R.id.pass_edt_text)

        signupBtn = findViewById(R.id.signup_btn)
        loginBtn = findViewById(R.id.login_btn)

        resetPasswordTv = findViewById(R.id.reset_pass_tv)

        auth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
            var email: String = emailEt.text.toString()
            var password: String = passwordEt.text.toString()

            var pos: Int = email.lastIndexOf("@")

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("email", email.substring(0, pos))
                        editor.apply()
                        editor.commit()


                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, GameActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        signupBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        resetPasswordTv.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
