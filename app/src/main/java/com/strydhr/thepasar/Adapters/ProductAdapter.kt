package com.strydhr.thepasar.Adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.strydhr.thepasar.Model.ProductDocument
import com.strydhr.thepasar.Model.StoreDocument
import com.strydhr.thepasar.R

class ProductAdapter(
    context: Context,
    var productList: List<ProductDocument>,
    val itemClick: (ProductDocument) -> Unit
): RecyclerView.Adapter<ProductAdapter.ViewHolder>(){
    private val context:Context

    inner class  ViewHolder(itemView: View, val itemClick: (ProductDocument) -> Unit):RecyclerView.ViewHolder(
        itemView
    ){
        private val productImage: ImageView
        private val productName: TextView
        private val productType: TextView
        private val productPrice: TextView

        fun bind(item: ProductDocument){
            Glide.with(itemView.context).load(item.product?.profileImage).into(productImage)
            productName.text = item.product?.name
            productType.text = item.product?.type
            productPrice.text = "RM ${(String.format("%.2f",item.product?.price))}"

        }

        init {
            productImage = itemView.findViewById(R.id.product_image)
            productName = itemView.findViewById(R.id.product_label)
            productPrice = itemView.findViewById(R.id.product_price)
            productType = itemView.findViewById(R.id.product_type)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view: View =  LayoutInflater.from(parent.context).inflate(
            R.layout.row_product_item,
            parent,
            false
        )
        return ViewHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        val itemName = productList[position]
        holder.bind(itemName)
        holder.itemView.setOnClickListener {
            itemClick(itemName)
        }
    }

    init {
        this.context = context
    }

}