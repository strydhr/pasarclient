package com.strydhr.thepasar.Services

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.strydhr.thepasar.Utilities.serverKey
import org.json.JSONException
import org.json.JSONObject

object NotificationServices {
    //..................................................
    //
    //                  CREATE
    //
    //.................................................
    fun sendNotification(context: Context, deviceToken:String, title:String, body:String){
        val url = "https://fcm.googleapis.com/fcm/send"
        val contentType = "application/json"

        val notification = JSONObject()
        val notifcationBody = JSONObject()
        try {
            notifcationBody.put("title", title)
            notifcationBody.put("message", body)
            notifcationBody.put("sound","default")
            notification.put("to", deviceToken)
            notification.put("notification", notifcationBody)

        } catch (e: JSONException) {
            Log.e(ContentValues.TAG, "onCreate: " + e.message)
        }


        val postRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,url, notification,
            Response.Listener { response ->
                Log.i(ContentValues.TAG, "onResponse: $response")

            },
            Response.ErrorListener {
                Log.i(ContentValues.TAG, "onErrorResponse: Didn't work")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        Volley.newRequestQueue(context).add(postRequest)


    }
}