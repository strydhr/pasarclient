package com.strydhr.thepasar.Controller.Fragments.View

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.strydhr.thepasar.Model.OrderDocument
import com.strydhr.thepasar.R
import kotlinx.android.synthetic.main.popup_rejected_comment.*

class PopupRejectedComment: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_rejected_comment)

        val orderStr = intent.getStringExtra("order")
        val order = Gson().fromJson<OrderDocument>(orderStr,OrderDocument::class.java)

        order_rejected_comment.text = order.order?.comment
    }
}