package com.strydhr.thepasar.Controller.Fragments

import android.graphics.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.strydhr.thepasar.Adapters.HistoryAdapter
import com.strydhr.thepasar.Adapters.SampleAdapter
import com.strydhr.thepasar.Model.ReceiptDocument

import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.PurchaseServices
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
        }

    }


}
