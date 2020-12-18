package com.strydhr.thepasar.Controller.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle
import kotlinx.android.synthetic.main.fragment_home_main.*


/**
 * A simple [Fragment] subclass.
 */
class HomeMain : Fragment() {

    lateinit var adapter:StoreAdapter
    var storeList: MutableList<StoreDocument> = ArrayList()
    lateinit var geopoint: GeoPoint

    private var currentToast: Toast? = null
    var hintCount = 0





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


      val rootView = inflater.inflate(R.layout.fragment_home_main, container, false)
        geopoint = GeoPoint(userGlobal?.l!![0], userGlobal?.l!![1])
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //For Initial hint
        if (!restorePrefData()){
            val displayMetrics = DisplayMetrics()
            activity?.getWindowManager()?.getDefaultDisplay()!!.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels

            println(height)
        var secondHintHeight = 0f
        var rowHeight = 0f
        if (height > 2300){
            rowHeight = 400f
            secondHintHeight = 450f

        }else if(height > 2000){
            rowHeight =  300f
            secondHintHeight = 350f
        }

            val targets = ArrayList<Target>()

            // first target
            val firstRoot = FrameLayout(activity!!)
            val first = layoutInflater.inflate(R.layout.layout_hints, firstRoot)
            val firstTarget = com.takusemba.spotlight.Target.Builder()
                .setAnchor(width.toFloat() - 70f, 200f)
                .setShape(Circle(100f))
                .setOverlay(first)
                .setOnTargetListener(object : OnTargetListener {
                    override fun onStarted() {
                        val text = first?.findViewById<TextView>(R.id.custom_text)
                        text!!.setText("Click this to change the search radius")
                    }

                    override fun onEnded() {

                    }
                })
                .build()
            4
            targets.add(firstTarget)

            val secondTarget = com.takusemba.spotlight.Target.Builder()
                .setAnchor(width.toFloat() / 2, secondHintHeight)
                .setShape(RoundedRectangle(rowHeight, width.toFloat(), 5f))
                .setOverlay(first)
                .setOnTargetListener(object : OnTargetListener {
                    override fun onStarted() {
                        val text = first?.findViewById<TextView>(R.id.custom_text)
                        val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)

                        params.setMargins(10, 700, 10, 10)
                        text?.layoutParams = params
                        text!!.setText("Choose the store to browse view their products")
                    }

                    override fun onEnded() {

                    }
                })
                .build()

            targets.add(secondTarget)

            val spotlight = Spotlight.Builder(activity!!)
                .setTargets(targets)
                .setBackgroundColorRes(R.color.blackOpacity)
                .setDuration(1000L)
                .setAnimation(DecelerateInterpolator(2f))
                .setOnSpotlightListener(object : OnSpotlightListener {
                    override fun onStarted() {

                    }

                    override fun onEnded() {


                    }
                })
                .build()

            spotlight.start()
            first.setOnClickListener {
                hintCount += 1
                println(hintCount)
                if (hintCount == 2){
                    println(hintCount)
                    savePrefsData()
                    spotlight.finish()
                }else{
                    spotlight.next()
                }

            }
        }
        // End of hint



        StoreServices.geoSearchStore(geopoint, 30.0){ storelist, query ->
            storeList = storelist
            query.removeAllListeners()
            adapter = StoreAdapter(context!!.applicationContext, storeList){

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
        inflater.inflate(R.menu.option_actionbar, menu)
        //
//        menuItemView = menu.findItem(R.id.action_option)
        //
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_option -> {
                var radiusPopup = Intent(
                    context!!.applicationContext,
                    PopupUpdateRadius::class.java
                )
                startActivityForResult(radiusPopup, 1)
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
                StoreServices.geoSearchStore(geopoint, newRadius!!.toDouble()){ storelist, query ->

                    query.removeAllListeners()
                    adapter.updateAdapter(storelist)

                }

            }
        }
    }

    // Initials hints
    private fun savePrefsData() {
        val pref = context!!.applicationContext.getSharedPreferences(
            "MyPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("seenHomemainHint", true)
        editor.commit()
    }


    private fun restorePrefData(): Boolean {
        val pref = context!!.applicationContext.getSharedPreferences(
            "MyPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        return pref.getBoolean("seenHomemainHint", false)
    }



}
