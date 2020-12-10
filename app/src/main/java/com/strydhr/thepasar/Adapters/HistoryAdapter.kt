package com.strydhr.thepasar.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.strydhr.thepasar.Model.ReceiptDocument
import com.strydhr.thepasar.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(
    context: Context,
    var orderList: ArrayList<ReceiptDocument>, val itemClick: (ReceiptDocument) -> Unit   ): RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
    private val context:Context

    inner class  ViewHolder(itemView: View, val itemClick: (ReceiptDocument) -> Unit):RecyclerView.ViewHolder(
        itemView
    ){
        private val storeName: TextView
        private val deliveryTime: TextView
        private val orderCount: TextView


        fun bind(item: ReceiptDocument){
            storeName.text = item.receipt?.storeName
            deliveryTime.text = dateStr(item.receipt?.deliveryTime!!)

            var total = 0
            for(items in item.receipt?.items!!){
                total += items.itemCount!!
            }
            orderCount.text = total.toString()


        }

        init {
            storeName = itemView.findViewById(R.id.history_store_name)
            deliveryTime = itemView.findViewById(R.id.history_delivery_time)
            orderCount = itemView.findViewById(R.id.history_count_number)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val view: View =  LayoutInflater.from(parent.context).inflate(
            R.layout.row_history_item,
            parent,
            false
        )
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val itemName = orderList[position]
        holder.bind(itemName)
        holder.itemView.setOnClickListener {
            itemClick(itemName)
        }
    }

    init {
        this.context = context
    }


    fun dateStr(date:Date): String {
        val dateformatter = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm", Locale.ENGLISH)
        val dateStr = dateformatter.format(date)


        return dateStr

    }



//    fun updateAdapter( list: ArrayList<StoreDocument>) {
//        storeList = list
//        // update adapter element like NAME, EMAIL e.t.c. here
//
//        // then in order to refresh the views notify the RecyclerView
//        notifyDataSetChanged()
//    }


}