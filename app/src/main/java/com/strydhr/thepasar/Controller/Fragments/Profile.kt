package com.strydhr.thepasar.Controller.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.auth.FirebaseAuth
import com.strydhr.thepasar.Adapters.ProfileAdapter
import com.strydhr.thepasar.Controller.LoginActivity

import com.strydhr.thepasar.R
import com.strydhr.thepasar.Utilities.googleApi
import com.strydhr.thepasar.Utilities.userGlobal
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class Profile : Fragment() {

    lateinit var adapter:ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_profile, container, false)

        Places.initialize(context!!.applicationContext, googleApi)

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(context!!.applicationContext)

        return rootView
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ProfileAdapter(userGlobal!!){
            if (it == "LOGOUT"){
                println("logout")


                val options =
                    arrayOf<CharSequence>("Log Out", "Cancel")
                val builder =
                    AlertDialog.Builder(activity!!)
                builder.setTitle("Are you sure you want to log out")
                builder.setItems(options) { dialog, item ->
                    if (options[item] == "Log Out") {

                        FirebaseAuth.getInstance().signOut()
                        userGlobal = null
                        var logoutIntent = Intent(context!!.applicationContext,LoginActivity::class.java)
                        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(logoutIntent)

                    } else if (options[item] == "Cancel") {
                        dialog.dismiss()
                    }
                }
                builder.show()


            }else if(it == "EDITADDRESS"){
                val AUTOCOMPLETE_REQUEST_CODE = 1

                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                val fields = listOf(Place.Field.ID, Place.Field.NAME)

                // Start the autocomplete intent.
                val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                    .setCountry("MY")
                    .build(context!!.applicationContext)
                startActivityForResult(intent, 1)
            }
        }
        profile_recyclerview.adapter = adapter
        val layoutManager = LinearLayoutManager(context!!.applicationContext)
        profile_recyclerview.layoutManager = layoutManager
        profile_recyclerview.setHasFixedSize(true)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "Place: ${place.name}, ${place.id}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i(TAG, status.statusMessage)
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
}
