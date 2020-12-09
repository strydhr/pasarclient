package com.strydhr.thepasar.Adapters

import android.widget.Button
import com.strydhr.thepasar.Model.OrderDocument
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.strydhr.thepasar.Model.StoreDocument
import com.strydhr.thepasar.R

class OrderAdapter(
    context: Context,
    var orderList: ArrayList<OrderDocument>,val itemClick: (OrderDocument) -> Unit   ): RecyclerView.Adapter<OrderAdapter.ViewHolder>(){
    private val context:Context

    inner class  ViewHolder(itemView: View, val itemClick: (OrderDocument) -> Unit):RecyclerView.ViewHolder(
        itemView
    ){
        private val statusIcon: ImageView
        private val storeName: TextView
        private val deliveryTime: TextView
        private val orderCount: TextView
        private val statusButton: Button

        fun bind(item: OrderDocument){


            storeName.text = item.order?.storeName
//            storeType.text = item.store?.type
            var total = 0
            for(items in item.order?.items!!){
                total += items.itemCount!!
            }
            orderCount.text = total.toString()

            if (item!!.order?.confirmationStatus == 1){
                statusIcon.visibility = View.INVISIBLE
                statusButton.visibility = View.INVISIBLE
            }else{
                statusButton.visibility = View.VISIBLE
                if (item!!.order?.confirmationStatus == 0){
                    statusIcon.visibility = View.VISIBLE
                    statusButton.text = "Rejected"
                }else if (item!!.order?.confirmationStatus == 2){

                }
            }
        }

        init {
            statusIcon = itemView.findViewById(R.id.order_status_icon)
            storeName = itemView.findViewById(R.id.order_store_name)
            deliveryTime = itemView.findViewById(R.id.order_delivery_time)
            orderCount = itemView.findViewById(R.id.order_count_number)
            statusButton = itemView.findViewById(R.id.order_statusbtn)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAdapter.ViewHolder {
        val view: View =  LayoutInflater.from(parent.context).inflate(
            R.layout.row_order_item,
            parent,
            false
        )
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderAdapter.ViewHolder, position: Int) {
        val itemName = orderList[position]
        holder.bind(itemName)
        holder.itemView.setOnClickListener {
            itemClick(itemName)
        }
    }

    init {
        this.context = context
    }

//    fun updateAdapter( list: ArrayList<StoreDocument>) {
//        storeList = list
//        // update adapter element like NAME, EMAIL e.t.c. here
//
//        // then in order to refresh the views notify the RecyclerView
//        notifyDataSetChanged()
//    }
}