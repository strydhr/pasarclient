package com.strydhr.thepasar.Controller

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.AuthServices
import com.strydhr.thepasar.Utilities.userGlobal
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 10);
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 10);
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        progressBar.visibility = View.INVISIBLE
        if (auth.currentUser != null){
            println(auth.currentUser!!.uid)
           AuthServices.getUserDetails(this, auth.currentUser!!.uid){ isSuccess,user ->
               userGlobal = user
               val intent = Intent(this, MainTabActivity::class.java)
               startActivity(intent)
               finish()
           }
        }
    }

    fun loginBtnClicked(view: View){
        enableProgressBar(true)
        val email = login_emailTF.text.toString()
        val password = login_passwordTF.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                if(task.isSuccessful) {
                    if (auth.currentUser != null){
                        AuthServices.getUserDetails(this, auth.currentUser!!.uid){ isSuccess,user ->
                            userGlobal = user
                            AuthServices.updateDeviceToken()
                            val intent = Intent(this, MainTabActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    val intent = Intent(this, MainTabActivity::class.java)
                    startActivity(intent)
                    finish()

                }else {
                    enableProgressBar(false)
                    Toast.makeText(this, "Login Failed : ${task.exception?.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            })
        }else{
            enableProgressBar(false)
            Toast.makeText(this,"Please fill in both fields", Toast.LENGTH_LONG).show()
        }



    }

    fun signupBtnClicked(view: View){
//        val createAccountIntent = Intent(this, SignupActivity::class.java)
//        startActivity(createAccountIntent)
    }

    fun resetBtnClicked(view: View){
//        val resetPasswordIntent = Intent(this, ResetPasswordActivity::class.java)
//        startActivity(resetPasswordIntent)
    }

    fun enableProgressBar(enable: Boolean){
        if (enable){

            progressBar.visibility = View.VISIBLE
            login_button.isEnabled = false
        }else{
            progressBar.visibility = View.INVISIBLE
            login_button.isEnabled = true
        }
    }
}
