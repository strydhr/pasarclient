package com.strydhr.thepasar.Adapters

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

class StoreAdapter(
    context: Context,
    var storeList: List<StoreDocument>,val itemClick: (StoreDocument) -> Unit   ): RecyclerView.Adapter<StoreAdapter.ViewHolder>(){
    private val context:Context

    inner class  ViewHolder(itemView: View, val itemClick: (StoreDocument) -> Unit):RecyclerView.ViewHolder(
        itemView
    ){
        private val storeImage: ImageView
        private val storeName: TextView
        private val storeType: TextView
        private val storeAddress: TextView

        fun bind(item: StoreDocument){
            Glide.with(itemView.context).load(item.store?.profileImage).into(storeImage)
            storeName.text = item.store?.name
            storeType.text = item.store?.type
            storeAddress.text = item.store?.location
        }

        init {
            storeImage = itemView.findViewById(R.id.store_image)
            storeName = itemView.findViewById(R.id.store_label)
            storeAddress = itemView.findViewById(R.id.store_address)
            storeType = itemView.findViewById(R.id.store_type)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreAdapter.ViewHolder {
        val view: View =  LayoutInflater.from(parent.context).inflate(
            R.layout.row_store_item,
            parent,
            false
        )
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return storeList.size
    }

    override fun onBindViewHolder(holder: StoreAdapter.ViewHolder, position: Int) {
        val itemName = storeList[position]
        holder.bind(itemName)
        holder.itemView.setOnClickListener {
            itemClick(itemName)
        }
    }

    init {
        this.context = context
    }

    fun updateAdapter( list: ArrayList<StoreDocument>) {
        storeList = list
        // update adapter element like NAME, EMAIL e.t.c. here

        // then in order to refresh the views notify the RecyclerView
        notifyDataSetChanged()
    }
}