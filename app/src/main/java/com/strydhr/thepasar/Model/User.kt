package com.strydhr.thepasar.Model

data class User(
    var uid:String? = null,
    var name:String? = null,
    var phone:String? = null,
    var address:String? = null,
    var unitNumber:String? = null,
    var l:ArrayList<Double>? = null,
    var g:String? = null,
    var profileImage:String? = null,
    @field:JvmField var isActivated:Boolean? = null,
    @field:JvmField var isActive:Boolean? = null,
    var deviceToken:String? = null

)

