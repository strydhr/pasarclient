package com.strydhr.thepasar.Utilities

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.strydhr.thepasar.Model.User
val serverApi = "AAAAUJA7Y08:APA91bG1yX3iyxgdhD3jFB-ag3TlD6HxixV_GAR3aOhz-QoIU7a2s51I_GpWUsCXiRaXSXvguU2kIysHmJfVK5wWlgpC2L17j9ROxba_avFWQ9ciVPZp9U-Aht95BMyGj0Jx2A_cDPz0"
val serverKey = "key=" + serverApi
val googleApi = googlekey

val db = Firebase.firestore
val storage = Firebase.storage

var userGlobal:User? = null
