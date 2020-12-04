package com.strydhr.thepasar.Services

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.GeoPoint
import com.strydhr.thepasar.Model.StoreDocument
import com.strydhr.thepasar.Utilities.db
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.extension.getAtLocation

object StoreServices {
    fun geoSearchStore(currentLocation:GeoPoint,radius:Double,complete:(ArrayList<StoreDocument>)-> Unit){
        var storeList: ArrayList<StoreDocument> = ArrayList()
        val colRef = db.collection("store")
        val geoSearch = GeoFirestore(colRef)

        val query = geoSearch.getAtLocation(currentLocation,radius){ document,exeption ->
            if (document != null){
                println(document)


            }
        }


//        val docRef = db.collection("store").whereEqualTo("ownerId",userId)
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null){
//                    if (document.isEmpty){
//                        complete(true,complaintList)
//                    }else{
//                        for (items in document){
//                            val complaint = items.toObject(Complaint::class.java)
//                            var complaintDoc = complaintDocument(items.id,complaint)
//                            complaintList.add(complaintDoc)
//                        }
//
//                        complete(true,complaintList)
//                    }
//
//
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(ContentValues.TAG, "Error getting documents.", exception)
//            }
    }
}