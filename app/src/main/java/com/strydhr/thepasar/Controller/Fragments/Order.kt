package com.strydhr.thepasar.Controller.Fragments

import android.content.Intent
import android.os.Bundle
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
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.Gson
import com.strydhr.thepasar.Adapters.OrderAdapter
import com.strydhr.thepasar.Controller.Fragments.View.PopupRejectedComment
import com.strydhr.thepasar.Model.OrderDocument
import com.strydhr.thepasar.Model.ReceiptDocument

import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.OrderServices
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.RoundedRectangle
import kotlinx.android.synthetic.main.fragment_order.*

/**
 * A simple [Fragment] subclass.
 * crashes when listner 2 = 0
 */
class Order : Fragment() {
    lateinit var listener: ListenerRegistration
    lateinit var listener2: ListenerRegistration
    lateinit var adapter: OrderAdapter

    var orderList:ArrayList<OrderDocument> = ArrayList()
    var receiptList:ArrayList<ReceiptDocument> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //For initial hints
        if (!restorePrefData()){
            val targets = ArrayList<Target>()

            // first target
            val firstRoot = FrameLayout(activity!!)
            val first = layoutInflater.inflate(R.layout.layout_order_hints, firstRoot)
            val firstTarget = com.takusemba.spotlight.Target.Builder()
                .setOverlay(first)
                .setOnTargetListener(object : OnTargetListener {
                    override fun onStarted() {
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


        // End of hints


        listener = OrderServices.realtimeListUpdate(){
            orderList = it
            if (orderList.size > 0){
            listener2 = OrderServices.realtimeListUpdate2 { receiptist ->
                receiptList = receiptist
                println(it.size)

                adapter = OrderAdapter(context!!.applicationContext, orderList,receiptList){

                    if (it.order?.confirmationStatus == 0){
                        var objStr = Gson().toJson(it)
                        var rejectPopup = Intent(
                            context!!.applicationContext,
                            PopupRejectedComment::class.java
                        )
                        rejectPopup.putExtra("order", objStr)
                        startActivity(rejectPopup)
                    }


                }
                order_recyclerview.adapter = adapter
                val layoutManager = LinearLayoutManager(context!!.applicationContext)
                order_recyclerview.layoutManager = layoutManager
                order_recyclerview.setHasFixedSize(true)
            }}

        }


    }

    override fun onStop() {
        super.onStop()
        listener.remove()
        if (orderList.size > 0) {
            listener2.remove()
        }
    }

    // Initials hints
    private fun savePrefsData() {
        val pref = context!!.applicationContext.getSharedPreferences(
            "MyPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("seenOrderHint", true)
        editor.commit()
    }


    private fun restorePrefData(): Boolean {
        val pref = context!!.applicationContext.getSharedPreferences(
            "MyPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        return pref.getBoolean("seenOrderHint", false)
    }

}
