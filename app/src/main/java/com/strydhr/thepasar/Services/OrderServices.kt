package com.strydhr.thepasar.Services

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.strydhr.thepasar.Model.Order
import com.strydhr.thepasar.Model.OrderDocument
import com.strydhr.thepasar.Utilities.db
import com.strydhr.thepasar.Utilities.userGlobal
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

object OrderServices {

    fun realtimeListUpdate(complete:(ArrayList<OrderDocument>)->Unit):ListenerRegistration{

        val orderList:ArrayList<OrderDocument> = ArrayList()
        val startOfDay = dateFinder("6:00")


        val dbRef = db.collection("orders").whereEqualTo("purchaserId", userGlobal?.uid).whereGreaterThanOrEqualTo("date",startOfDay)
        return dbRef.addSnapshotListener { snapshot, error ->
            if(error == null){
                for (dc in snapshot!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val order = dc.document.toObject(Order::class.java)
                            val orderDoc = OrderDocument(dc.document.id,order)
                            orderList.add(orderDoc)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val newOrder = dc.document.toObject(Order::class.java)
                            val index = orderList.indexOfFirst { it.documentId == dc.document.id }
                            if (index != null){
                                orderList[index].order = newOrder
                            }

                        }
//                        DocumentChange.Type.REMOVED -> Log.d(TAG, "Removed city: ${dc.document.data}")
                    }
                    complete(orderList)
                }
            }
        }


    }

    fun dateFinder(time:String):Date{
        val dateformatter = SimpleDateFormat("yyyy-MM-dd")
        val datehalf = dateformatter.format(Date())
        val formatter = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm", Locale.ENGLISH)
        val dateStr = datehalf + " at " + time
        val date = formatter.parse(dateStr)

        return date

    }
}