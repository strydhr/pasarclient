package com.strydhr.thepasar.Controller.Fragments

import android.graphics.*
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.strydhr.thepasar.Adapters.HistoryAdapter
import com.strydhr.thepasar.Adapters.SampleAdapter
import com.strydhr.thepasar.Model.ReceiptDocument

import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.PurchaseServices
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle
import kotlinx.android.synthetic.main.fragment_history.*

/**
 * A simple [Fragment] subclass.
 */
class History : Fragment() {

    var receiptList:ArrayList<ReceiptDocument> = ArrayList()
    lateinit var adapter: SampleAdapter
    lateinit var recyclerView: RecyclerView

    private val p = Paint()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = rootView.findViewById(R.id.history_recyclerview)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        PurchaseServices.listPastPurchases {
            it.sortByDescending { it.receipt?.deliveryTime }
            adapter = SampleAdapter(context!!.applicationContext,it,recyclerView)
            history_recyclerview.adapter = adapter
            val layoutManager = LinearLayoutManager(context!!.applicationContext)
            history_recyclerview.layoutManager = layoutManager
            history_recyclerview.setHasFixedSize(true)

            //For initial hints
            if (!restorePrefData()){
                if(it.size > 0){
                    val displayMetrics = DisplayMetrics()
                    activity?.windowManager?.defaultDisplay!!.getMetrics(displayMetrics)
                    val height = displayMetrics.heightPixels
                    val width = displayMetrics.widthPixels

                    println(height) //initialize hint position for multiple phone
                    var secondHintHeight = 0f
                    var rowHeight = 0f
                    var textHeight = 0
                    if (height > 2300){
                        rowHeight = 400f
                        secondHintHeight = 450f
                        textHeight = 700
                    }else if(height > 2000){
                        rowHeight =  300f
                        secondHintHeight = 350f
                        textHeight = 700
                    }else if(height > 1100){
                        rowHeight =  200f
                        secondHintHeight = 230f
                        textHeight = 350
                    }
                    else if(height > 700){
                        rowHeight =  90f
                        secondHintHeight = 130f
                        textHeight = 180
                    }

                    val targets = ArrayList<Target>()

                    // first target
                    val firstRoot = FrameLayout(activity!!)
                    val first = layoutInflater.inflate(R.layout.layout_addproduct_hints, firstRoot)
                    val firstTarget = com.takusemba.spotlight.Target.Builder()
                        .setAnchor(width.toFloat() / 2, secondHintHeight)
                        .setShape(RoundedRectangle(rowHeight, width.toFloat(), 5f))
                        .setOverlay(first)
                        .setOnTargetListener(object : OnTargetListener {
                            override fun onStarted() {
                                val container = first.findViewById<LinearLayout>(R.id.hint_container)
                                val text = first?.findViewById<TextView>(R.id.addprod_hint_text)
                                val params = RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT)

                                params.setMargins(10, textHeight, 10, 10)
                                container?.layoutParams = params
                                text!!.text = "Swipe left to lodge a complaint on past purchases"
                            }

                            override fun onEnded() {

                            }
                        })
                        .build()

                    targets.add(firstTarget)

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
                        spotlight.finish()
                        savePrefsData()

                    }
                }
            }

            //End of hint
        }

    }

    // Initials hints
    private fun savePrefsData() {
        val pref = context!!.applicationContext.getSharedPreferences(
            "MyPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("seenHistoryHint", true)
        editor.commit()
    }


    private fun restorePrefData(): Boolean {
        val pref = context!!.applicationContext.getSharedPreferences(
            "MyPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        return pref.getBoolean("seenHistoryHint", false)
    }

}
