package com.strydhr.thepasar.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.strydhr.thepasar.Model.itemPurchasing
import com.strydhr.thepasar.R

// Need to add notification
class CartAdapter(
    context: Context,
    var cartList: ArrayList<itemPurchasing>): RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    private val context:Context

    inner class  ViewHolder(itemView: View):RecyclerView.ViewHolder(
        itemView
    ){
        private val productName: TextView
        private val productType: TextView
        private val productCount: TextView

        fun bind(item: itemPurchasing){
            productName.text = item.productName
            productType.text = "RM ${(String.format("%.2f",item.productPrice))}"
            productCount.text = item.itemCount.toString()
        }

        init {
            productName = itemView.findViewById(R.id.cartitem_label)
            productType = itemView.findViewById(R.id.cartitem_type)
            productCount = itemView.findViewById(R.id.cartitem_count)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val view: View =  LayoutInflater.from(parent.context).inflate(
            R.layout.row_cart_item,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: CartAdapter.ViewHolder, position: Int) {
        val itemName = cartList[position]
        holder.bind(itemName)

    }

    fun removeItem(position: Int):itemPurchasing{
        var name = cartList[position]
        cartList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, cartList.size)

        return name!!
    }

    init {
        this.context = context
    }

}