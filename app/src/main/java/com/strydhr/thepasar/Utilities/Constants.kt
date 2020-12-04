package com.strydhr.thepasar.Utilities

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.strydhr.thepasar.Model.User

val db = Firebase.firestore
val storage = Firebase.storage

var userGlobal:User? = null