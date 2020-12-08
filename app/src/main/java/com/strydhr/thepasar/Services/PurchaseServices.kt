package com.strydhr.thepasar.Services

import com.strydhr.thepasar.Model.Order
import com.strydhr.thepasar.Utilities.db

object PurchaseServices {
    fun confirmPurchase(receipt:Order){
        println(receipt)
        db.collection("orders").add(receipt).addOnSuccessListener {

        }
    }
}