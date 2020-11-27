package com.cahstudio.instaapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cahstudio.instaapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var mAuth = FirebaseAuth.getInstance()
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private lateinit var mPref: SharedPreferences
    private lateinit var mPrefEditor: SharedPreferences.Editor
    private lateinit var mRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialize()
        checkUserLoggedIn()
    }

    fun initialize(){
        mAuth = FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference
        mPref = getSharedPreferences("inst_app", Context.MODE_PRIVATE)
        mPrefEditor = getSharedPreferences("inst_app", Context.MODE_PRIVATE).edit()

        login_btnLoginUser.setOnClickListener(this)
        login_tvRegister.setOnClickListener(this)
    }

    fun checkForm(){
        val email = login_etEmail.text.toString()
        val password = login_etPassword.text.toString()
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Lengkapi email atau password", Toast.LENGTH_SHORT).show()
        }else{
            login_btnLoginUser.text = ""
            login_progressbar.visibility = View.VISIBLE
            login(email, password)
        }
    }

    fun login(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!it.isSuccessful){
                login_btnLoginUser.text = "Masuk"
                login_progressbar.visibility = View.GONE
                Toast.makeText(this, "Login gagal, email atau password salah", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkUserLoggedIn(){
        mAuthStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser != null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mAuth.addAuthStateListener(mAuthStateListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        mAuth.removeAuthStateListener(mAuthStateListener)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_btnLoginUser -> {
                checkForm()
            }
            R.id.login_tvRegister -> {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }
        }
    }
}