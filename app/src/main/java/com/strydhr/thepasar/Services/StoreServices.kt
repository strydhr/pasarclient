package com.strydhr.thepasar.Services

import android.content.ContentValues
import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import com.strydhr.thepasar.Model.Store
import com.strydhr.thepasar.Model.StoreDocument
import com.strydhr.thepasar.Utilities.db
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.GeoQuery
import org.imperiumlabs.geofirestore.extension.getAtLocation
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener
import org.imperiumlabs.geofirestore.listeners.GeoQueryEventListener

object StoreServices {
    fun geoSearchStore(currentLocation:GeoPoint,radius:Double,complete:(ArrayList<StoreDocument>,GeoQuery)-> Unit){
        var storeList: ArrayList<StoreDocument> = ArrayList()
        val colRef = db.collection("store")
        val geoSearch = GeoFirestore(colRef)
        val query = geoSearch.queryAtLocation(currentLocation,radius)
        println(currentLocation)
        query.addGeoQueryDataEventListener(object :GeoQueryDataEventListener{
            override fun onDocumentChanged(documentSnapshot: DocumentSnapshot, location: GeoPoint) {

            }

            override fun onDocumentEntered(documentSnapshot: DocumentSnapshot, location: GeoPoint) {
                println(documentSnapshot)
                if (documentSnapshot != null) {
                    if (!documentSnapshot.exists()){

                        complete(storeList,query)
                    }else{
                        val store = documentSnapshot.toObject(Store::class.java)
                        val storeDoc = StoreDocument(documentSnapshot.id,store)
                        storeList.add(storeDoc)
                        complete(storeList,query)
                    }

                }
            }

            override fun onDocumentExited(documentSnapshot: DocumentSnapshot) {

            }

            override fun onDocumentMoved(documentSnapshot: DocumentSnapshot, location: GeoPoint) {

            }

            override fun onGeoQueryError(exception: Exception) {

            }

            override fun onGeoQueryReady() {

            }

        })
    }
}