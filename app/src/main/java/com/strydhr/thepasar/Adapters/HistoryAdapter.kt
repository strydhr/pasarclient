package com.strydhr.thepasar.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.strydhr.thepasar.Controller.Fragments.View.PopupLodgeComplaint
import com.strydhr.thepasar.Controller.Fragments.View.PopupRejectedComment
import com.strydhr.thepasar.Model.OrderDocument
import com.strydhr.thepasar.Model.ReceiptDocument
import com.strydhr.thepasar.R
import com.tr4android.recyclerviewslideitem.SwipeAdapter
import com.tr4android.recyclerviewslideitem.SwipeConfiguration
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class SampleAdapter(private val mContext: Context,dataset:ArrayList<ReceiptDocument>, private val mRecyclerView: RecyclerView ) :
    SwipeAdapter(), View.OnClickListener {
    var mDataset: ArrayList<ReceiptDocument> = dataset
    var colors = intArrayOf(
        R.color.turquoiseBtn,
        R.color.grey
    )

    inner class SampleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var contentView: RelativeLayout
//        var avatarView: View
        var storeLabel: TextView
        var deliveryTime: TextView
        var orderCount: TextView

        init {
            contentView = view.findViewById<View>(R.id.history_contentView) as RelativeLayout
//            avatarView = view.findViewById(R.id.avatarView)
            storeLabel = view.findViewById<View>(R.id.history_store_name) as TextView
            deliveryTime = view.findViewById<View>(R.id.history_delivery_time) as TextView
            orderCount = view.findViewById<View>(R.id.history_count_number) as TextView
            contentView.setOnClickListener(this@SampleAdapter)
        }
    }

    override fun onCreateSwipeViewHolder(parent: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_history_item, parent, true)
        return SampleViewHolder(v)
    }

    override fun onBindSwipeViewHolder(swipeViewHolder: RecyclerView.ViewHolder, i: Int) {
        val sampleViewHolder = swipeViewHolder as SampleViewHolder
        val drawable = ShapeDrawable(OvalShape())
        drawable.paint.color =
            mContext.resources.getColor(colors[(Math.random() * (colors.size - 1)).toInt()])
//        sampleViewHolder.avatarView.setBackgroundDrawable(drawable)
        sampleViewHolder.storeLabel.text = mDataset[i].receipt?.storeName
        sampleViewHolder.deliveryTime.text = dateStr(mDataset[i].receipt?.deliveryTime!!)
        var total = 0
        for(items in mDataset[i].receipt?.items!!){
            total += items.itemCount!!
        }
        sampleViewHolder.orderCount.text = total.toString()

    }

    override fun onCreateSwipeConfiguration(context: Context, position: Int): SwipeConfiguration {
        return SwipeConfiguration.Builder(context)
            .setLeftBackgroundColorResource(R.color.turquoiseBtn)
//            .setRightBackgroundColorResource(R.color.color_mark)
            .setDrawableResource(R.drawable.delete)
//            .setRightDrawableResource(R.drawable.ic_done_white_24dp)
            .setLeftUndoable(false)
//            .setLeftUndoDescription(R.string.action_deleted)
            .setDescriptionTextColorResource(android.R.color.white)
            .setLeftSwipeBehaviour(SwipeConfiguration.SwipeBehaviour.RESTRICTED_SWIPE)
            .setRightSwipeBehaviour(SwipeConfiguration.SwipeBehaviour.NO_SWIPE)
            .build();
    }

    override fun onSwipe(position: Int, direction: Int) {
        if (direction == SWIPE_LEFT) {

            val receipt = mDataset[position]
            val objStr = Gson().toJson(receipt)

            var lodgeComplaintPopup = Intent(mContext.applicationContext
                ,
                ::class.java
            )
            lodgeComplaintPopup.putExtra("receipt", objStr)
            lodgeComplaintPopup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(lodgeComplaintPopup)


        }
    }

    override fun onClick(view: View) {
        // We need to get the parent of the parent to actually have the proper view
        val position = mRecyclerView.getChildAdapterPosition((view.parent.parent as View))
        val toast =
            Toast.makeText(mContext, "Clicked item at position $position", Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

        fun dateStr(date: Date): String {
        val dateformatter = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm", Locale.ENGLISH)
        val dateStr = dateformatter.format(date)


        return dateStr

    }

//    init {
//        // create dummy dataset
//        mDataset = ArrayList()
//        for (i in 0..24) {
//            mDataset.add("person" + (i + 1).toString() + "@sample.com")
//        }
//    }
}

//class HistoryAdapter(
//    context: Context,
//    var orderList: ArrayList<ReceiptDocument>, val itemClick: (ReceiptDocument) -> Unit   ): RecyclerView.Adapter<HistoryAdapter.ViewHolder>(){
//    private val context:Context
//
//    inner class  ViewHolder(itemView: View, val itemClick: (ReceiptDocument) -> Unit):RecyclerView.ViewHolder(
//        itemView
//    ){
//        private val storeName: TextView
//        private val deliveryTime: TextView
//        private val orderCount: TextView
//
//
//        fun bind(item: ReceiptDocument){
//            storeName.text = item.receipt?.storeName
//            deliveryTime.text = dateStr(item.receipt?.deliveryTime!!)
//
//            var total = 0
//            for(items in item.receipt?.items!!){
//                total += items.itemCount!!
//            }
//            orderCount.text = total.toString()
//
//
//        }
//
//        init {
//            storeName = itemView.findViewById(R.id.history_store_name)
//            deliveryTime = itemView.findViewById(R.id.history_delivery_time)
//            orderCount = itemView.findViewById(R.id.history_count_number)
//        }
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
//        val view: View =  LayoutInflater.from(parent.context).inflate(
//            R.layout.row_history_item,
//            parent,
//            false
//        )
//        return ViewHolder(view, itemClick)
//    }
//
//    override fun getItemCount(): Int {
//        return orderList.size
//    }
//
//    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
//        val itemName = orderList[position]
//        holder.bind(itemName)
//        holder.itemView.setOnClickListener {
//            itemClick(itemName)
//        }
//    }
//
//    init {
//        this.context = context
//    }
//
//
//    fun dateStr(date:Date): String {
//        val dateformatter = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm", Locale.ENGLISH)
//        val dateStr = dateformatter.format(date)
//
//
//        return dateStr
//
//    }
//
//
//
//}