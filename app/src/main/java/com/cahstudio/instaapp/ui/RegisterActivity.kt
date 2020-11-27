package com.cahstudio.instaapp.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.cahstudio.instaapp.R
import com.cahstudio.instaapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private var mAuth = FirebaseAuth.getInstance()
    private var mRef = FirebaseDatabase.getInstance().reference

    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initialize()
    }

    fun initialize(){
        mAuth = FirebaseAuth.getInstance()

        register_btnRegis.setOnClickListener(this)

//        checkUserLoggedIn()
    }

    fun checkUserLoggedIn(){
        mAuthStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser != null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    fun checkForm(){
        val name = register_etName.text.toString()
        val email = register_etEmail.text.toString()
        val password = register_etPassword.text.toString()
        val confirmPassword = register_etConfirmPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(this, "Isi data dengan benar", Toast.LENGTH_SHORT).show()
        }else if (password != confirmPassword){
            Toast.makeText(this, "Konfirmasi password salah", Toast.LENGTH_SHORT).show()
        }else{
            register_progressbar.visibility = View.VISIBLE
            register_btnRegis.text = ""
            register(name, email, password)
        }
    }

    fun register(name: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
            if (it.isSuccessful){
                val authUser = it.result?.user
                val user = User(authUser?.uid, name, email)

                authUser?.uid?.let { it1 -> mRef.child("users").child(it1).setValue(user) }
                Toast.makeText(this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                register_progressbar.visibility = View.GONE
                register_btnRegis.text = "Daftar"
                Toast.makeText(this, "Pendaftaran gagal, coba lagi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.register_btnRegis -> {
                checkForm()
            }
            R.id.register_tvLogin -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}