package com.strydhr.thepasar.Services

import android.content.Context
import android.util.Log
import androidx.constraintlayout.widget.Constraints
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.strydhr.thepasar.Model.User
import com.strydhr.thepasar.Utilities.db
import com.strydhr.thepasar.Utilities.userGlobal
import java.lang.Exception

object AuthServices {

    private lateinit var auth: FirebaseAuth


    fun registerNewUser(email:String,password: String,complete: (Boolean, Exception?) -> Unit){
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                complete(true,null)
            }else{
                complete(false, task.exception!!)
            }
        }
    }

    fun getUserDetails(context: Context, userId:String, complete:(Boolean, User)->Unit){

        val docRef = db.collection("User").document(userId)
        docRef.get()
            .addOnCompleteListener { document ->
                if (document.isSuccessful){
                    val doc = document.result
                    if (doc!!.exists()){
                        println("got")
                        val user = document.result?.toObject(User::class.java)

                        complete(true,user!!)
                    }else{
                        println("error")
                        val user = User()
                        complete(false,user)
                    }


                }else{
                    Log.w(Constraints.TAG, "Error getting documents.", document.exception)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(Constraints.TAG, "Error getting documents.", exception)

            }
    }

    fun updateUserAddress(address:String,coor:ArrayList<Double>,geoHash:String,complete: (Boolean) -> Unit){
        var docRef =  db.collection("User").document(userGlobal?.uid!!).update(
            "address",address,"l",coor,"g",geoHash)
        docRef.addOnSuccessListener {
            complete(true)
        }
    }

    fun updateUserPhone(phone:String,complete: (Boolean) -> Unit){
        var docRef =  db.collection("User").document(userGlobal?.uid!!).update(
            "phone",phone)
        docRef.addOnSuccessListener {
            complete(true)
        }
    }


    fun updateDeviceToken(){
        auth = FirebaseAuth.getInstance()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(Constraints.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            var docRef = db.collection("User").document(auth.currentUser?.uid!!).update(
                "deviceToken",token.toString()
            )
            docRef.addOnSuccessListener {

            }
        })
    }
}