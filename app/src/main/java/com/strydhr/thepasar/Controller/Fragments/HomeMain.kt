package com.strydhr.thepasar.Controller.Fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.GeoPoint
import com.google.gson.Gson
import com.strydhr.thepasar.Adapters.StoreAdapter
import com.strydhr.thepasar.Controller.Fragments.View.PopupUpdateRadius
import com.strydhr.thepasar.Controller.Fragments.View.StoreProduct
import com.strydhr.thepasar.Model.StoreDocument

import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.StoreServices
import com.strydhr.thepasar.Utilities.userGlobal
import kotlinx.android.synthetic.main.fragment_home_main.*

/**
 * A simple [Fragment] subclass.
 */
class HomeMain : Fragment() {

    lateinit var adapter:StoreAdapter
    var storeList: MutableList<StoreDocument> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //All asdasd
        return inflater.inflate(R.layout.fragment_home_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val geopoint = GeoPoint(userGlobal?.l!![0], userGlobal?.l!![1])
        StoreServices.geoSearchStore(geopoint, 30.0){ storelist,query ->
            storeList = storelist
            query.removeAllListeners()
            adapter = StoreAdapter(context!!.applicationContext,storeList){

                var objStr = Gson().toJson(it)
                println(objStr)

                val bundle = Bundle()
                bundle.putString("store", objStr)
                val fragInfo = StoreProduct()
                fragInfo.arguments = bundle


                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragInfo).addToBackStack(null)
                    .commit()

            }
            viewstore_recyclerview.adapter = adapter
            val layoutManager = LinearLayoutManager(context!!.applicationContext)
            viewstore_recyclerview.layoutManager = layoutManager
            viewstore_recyclerview.setHasFixedSize(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_actionbar,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_option ->{
                var radiusPopup = Intent(context!!.applicationContext,PopupUpdateRadius::class.java)
                startActivityForResult(radiusPopup,1)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                val newRadius = data?.getStringExtra("newRadius")
                val geopoint = GeoPoint(userGlobal?.l!![0], userGlobal?.l!![1])
                StoreServices.geoSearchStore(geopoint, newRadius!!.toDouble()){ storelist,query ->

                    query.removeAllListeners()
                    adapter.updateAdapter(storelist)

                }

            }
        }
    }

}
