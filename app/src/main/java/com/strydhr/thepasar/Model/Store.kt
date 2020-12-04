package com.strydhr.thepasar.Model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*
import kotlin.collections.ArrayList

data class Store(
    var uid:String? = null,
    var type:String? = null,
    var name:String? = null,
    var location:String? = null,
    var l:ArrayList<Double>? = null,
    var g:String? = null,
    @ServerTimestamp var startDate: Date? = null,
    var ownerId:String? = null,
    var profileImage:String? = null,
    @field:JvmField var isEnabled:Boolean? = null,
    @field:JvmField var isClosed:Boolean? = null,
    var deviceToken:String? = null


)

data class StoreDocument(
    var documentId:String? = null,
    var store:Store? = null
)