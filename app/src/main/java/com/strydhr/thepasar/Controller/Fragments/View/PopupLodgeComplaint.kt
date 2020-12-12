package com.strydhr.thepasar.Controller.Fragments.View

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.strydhr.thepasar.Model.ReceiptDocument
import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.PurchaseServices
import kotlinx.android.synthetic.main.popup_lodge_complaint.*

class PopupLodgeComplaint: AppCompatActivity() {

    lateinit var confirmBtn: Button
    lateinit var complaint: EditText
    lateinit var receipt: ReceiptDocument


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_lodge_complaint)
        val orderStr = intent.getStringExtra("receipt")
        receipt = Gson().fromJson<ReceiptDocument>(orderStr,ReceiptDocument::class.java)

        confirmBtn = findViewById(R.id.lodge_confirmBtn)
        complaint = findViewById(R.id.lodge_complaint)

        confirmBtn.setOnClickListener {
            errorHandler(complaint.text.toString())
        }

    }

    private fun errorHandler(complaint:String){
        if (complaint.isNotEmpty()){
            PurchaseServices.lodgeComplaint(receipt,complaint){
                if(it){
                    finish()
                }
            }
        }
    }
}