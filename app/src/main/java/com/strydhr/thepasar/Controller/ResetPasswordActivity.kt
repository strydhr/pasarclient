package com.strydhr.thepasar.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.AuthServices
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
    }

    fun resetBtnClicked(view: View){
        AuthServices.resetPassword(reset_emailTF.text.toString()){ isSuccess ->
            if (isSuccess){
                val options =
                    arrayOf<CharSequence>("Ok")
                val builder =
                    AlertDialog.Builder(this)
                builder.setTitle("An email have been sent to you to reset your password")
                builder.setItems(options) { dialog, item ->
                    if (options[item] == "Ok") {
                        val loginIntent = Intent(this, LoginActivity::class.java)
                        startActivity(loginIntent)
                        finish()
                        dialog.dismiss()
                    }
                }
                builder.show()
            }
        }
    }
}
