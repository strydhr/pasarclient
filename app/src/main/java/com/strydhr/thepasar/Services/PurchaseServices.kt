package com.strydhr.thepasar.Services

import android.util.Log
import androidx.constraintlayout.widget.Constraints
import com.strydhr.thepasar.Model.Complaint
import com.strydhr.thepasar.Model.Order
import com.strydhr.thepasar.Model.ReceiptDocument
import com.strydhr.thepasar.Model.Receipts
import com.strydhr.thepasar.Utilities.db
import com.strydhr.thepasar.Utilities.userGlobal
import java.util.*
import kotlin.collections.ArrayList

object PurchaseServices {
    fun confirmPurchase(receipt:Order){
        println(receipt)
        db.collection("orders").add(receipt).addOnSuccessListener {

        }
    }

    fun listPastPurchases(complete:(ArrayList<ReceiptDocument>)-> Unit){
        var receiptList:ArrayList<ReceiptDocument> = ArrayList()
        val docRef = db.collection("receipt").whereEqualTo("purchaserId", userGlobal?.uid).whereEqualTo("caseClosed",true)
        docRef.get().addOnSuccessListener { document ->
            if (document != null){
                for (items in document){
                    val receipt = items.toObject(Receipts::class.java)
                    val receiptDoc = ReceiptDocument(items.id,receipt)
                    receiptList.add(receiptDoc)
                }
                complete(receiptList)
            }
        }
            .addOnFailureListener { exception ->
                Log.w(Constraints.TAG, "Error getting documents.", exception)
            }
    }

    fun lodgeComplaint(receipt:ReceiptDocument,complaint:String,complete: (Boolean)-> Unit){
        val complaint = Complaint(receipt.receipt?.items, Date(),receipt.receipt?.deliveryTime,receipt.receipt?.purchaserId,receipt.receipt?.purchaserName,receipt.receipt?.purchaserAddress,receipt.receipt?.purchaserPhone,receipt.receipt?.storeId,receipt.receipt?.storeName,receipt.receipt?.ownerId,receipt.documentId,complaint,false)
        db.collection("complaints").add(complaint).addOnSuccessListener {
            complete(true)
        }
    }
}