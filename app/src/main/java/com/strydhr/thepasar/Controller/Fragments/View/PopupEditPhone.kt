package com.strydhr.thepasar.Controller.Fragments.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.AuthServices
import com.strydhr.thepasar.Utilities.userGlobal

class PopupEditPhone:AppCompatActivity() {

    lateinit var confirmBtn: Button
    lateinit var phone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_edit_phone)

        confirmBtn = findViewById(R.id.editphone_confirmBtn)
        phone = findViewById(R.id.edit_phone_tf)


        phone.setText(userGlobal?.phone)

        confirmBtn.setOnClickListener {
            errorHandler(phone.text.toString())
        }
    }

    private fun errorHandler(phoneNumber:String){
        if (phoneNumber.isEmpty()){
            val toast = Toast.makeText(
                applicationContext,
                "Please your mobile number", Toast.LENGTH_SHORT
            )
            toast.show()
        }else{
            AuthServices.updateUserPhone(phoneNumber){
                if (it){
                    userGlobal?.phone = phoneNumber
                    val fromEditPhone = Intent()
//                    fromEditPhone.putExtra("hasChangedPhone",true)
                    setResult(Activity.RESULT_OK,fromEditPhone)
                    finish()
                }
            }
        }
    }
}