package com.strydhr.thepasar.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.AuthServices
import kotlinx.android.synthetic.main.activity_signup.*
import java.lang.Exception

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }

    fun proceedBtnPressed(view: View){
        errorHandler(signup_name_tf.text.toString(),signup_email_tf.text.toString(),signup_password_tf.text.toString(),signup_confirmpassword_tf.text.toString())
//        val createAccountIntent = Intent(this, SignupLocationActivity::class.java)
//        createAccountIntent.putExtra("username",signup_name_tf.text.toString())
//        startActivity(createAccountIntent)
    }

    private fun errorHandler(username:String,email:String,password:String,confirmPw:String){
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPw.isEmpty()){
            Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_LONG).show()


        }else{
            if(confirmPw != password){
                Toast.makeText(this, "Your confirm field in not the same", Toast.LENGTH_LONG).show()
            }else{
                AuthServices.registerNewUser(email,password){ isSuccess, exception ->
                    if(isSuccess){
                        val createAccountIntent = Intent(this, SignupLocationActivity::class.java)
                        createAccountIntent.putExtra("username",username)
                        startActivity(createAccountIntent)
                    }else {
                        println(exception.toString())
                        handleError(exception!!)
                    }
                }
            }

        }

    }

    fun handleError(e: Exception){
        Toast.makeText(this, "${e.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}
