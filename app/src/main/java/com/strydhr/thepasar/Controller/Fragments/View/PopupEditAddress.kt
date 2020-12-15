package com.strydhr.thepasar.Controller.Fragments.View

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fonfon.kgeohash.GeoHash
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.AuthServices
import com.strydhr.thepasar.Utilities.googleApi
import com.strydhr.thepasar.Utilities.userGlobal
import java.lang.reflect.Array
import java.util.*
import kotlin.collections.ArrayList

class PopupEditAddress: AppCompatActivity() {

    lateinit var confirmBtn: Button
    lateinit var address: EditText
    lateinit var unitNumber: EditText

    lateinit var deliveryAddress:String
    var coordinate: ArrayList<Double> = ArrayList()
    lateinit var geoHash:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_edit_address)


        confirmBtn = findViewById(R.id.edit_confirmBtn)
        address = findViewById(R.id.edit_address_tf)
        unitNumber = findViewById(R.id.edit_unitnumber_tf)
        Places.initialize(applicationContext, googleApi)

        val placesClient = Places.createClient(applicationContext)

        address.setOnClickListener{
            val AUTOCOMPLETE_REQUEST_CODE = 1

            // Set the fields to specify which types of place data to
            // return after the user has made a selection.
            val fields = listOf(Place.Field.ID, Place.Field.NAME)

            // Start the autocomplete intent.
//            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));

            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, listOf(Place.Field.ADDRESS_COMPONENTS, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS))
                .setCountry("MY")
                .build(applicationContext)
            startActivityForResult(intent, 1)
        }

        confirmBtn.setOnClickListener {
            errorHandler(address.text.toString())
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        println("hello")
                        val place = Autocomplete.getPlaceFromIntent(data)
                        val placeStr = place.name

                        address.setText(placeStr)
                        deliveryAddress = place.address!!
                        coordinate.add( place.latLng!!.latitude)
                        coordinate.add(place.latLng!!.longitude)
                        geoHash = GeoHash(place.latLng!!.latitude,place.latLng!!.longitude,10).toString()
                        Log.i(ContentValues.TAG, "Place: ${place.name}, ${place.address},${place.latLng?.latitude}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {

                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(ContentValues.TAG, status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun errorHandler(address:String){
        if (address.isEmpty()){
            val toast = Toast.makeText(
                applicationContext,
                "Please fill your delivery address", Toast.LENGTH_SHORT
            )
            toast.show()
        }else{
            var addressStr:String
            if (unitNumber.text.isEmpty()){
                addressStr = deliveryAddress
            }else{
                addressStr = unitNumber.text.toString() + ", " + deliveryAddress
            }

            AuthServices.updateUserAddress(addressStr,coordinate,geoHash){
                if (it){
                    userGlobal?.address = addressStr
                    userGlobal?.g = geoHash
                    userGlobal?.l = coordinate
                    val fromEditAddress = Intent()
                    setResult(Activity.RESULT_OK,fromEditAddress)
                    finish()
                }
            }
        }
    }
}