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

//        enableSwipe()
    }
//
//        private fun enableSwipe() {
//        val simpleItemTouchCallback =
//            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {
//
//                override fun onMove(
//                    recyclerView: RecyclerView,
//                    viewHolder: RecyclerView.ViewHolder,
//                    target: RecyclerView.ViewHolder
//                ): Boolean {
//                    return false
//                }
//
//                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                    val position = viewHolder.adapterPosition
//
//
//                    var removedItem = adapter!!.removeItem(position)
//                    receiptList = receiptList?.filter { it != removedItem } as ArrayList<ReceiptDocument>
////                    var itemToDelete = removedItem.content
////                    var list = realm.where(RealmChecklist::class.java).equalTo("content",itemToDelete).findFirst()
////                    realm.beginTransaction()
////                    list?.deleteFromRealm()
////                    realm.commitTransaction()
////                    adapter.notifyDataSetChanged()
//
//
//                }
//
//                override fun onChildDraw(
//                    c: Canvas,
//                    recyclerView: RecyclerView,
//                    viewHolder: RecyclerView.ViewHolder,
//                    dX: Float,
//                    dY: Float,
//                    actionState: Int,
//                    isCurrentlyActive: Boolean
//                ) {
//
//                    val icon: Bitmap
//                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//
//                        val itemView = viewHolder.itemView
//                        val height = itemView.bottom.toFloat() - itemView.top.toFloat()
//                        val width = height / 3
//
//                        if (dX > 0) {
//                            p.color = Color.parseColor("#388E3C")
//                            val background =
//                                RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
//                            c.drawRect(background, p)
//                            icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
//                            val icon_dest = RectF(
//                                itemView.left.toFloat() + width,
//                                itemView.top.toFloat() + width,
//                                itemView.left.toFloat() + 2 * width,
//                                itemView.bottom.toFloat() - width
//                            )
//                            c.drawBitmap(icon, null, icon_dest, p)
//                        } else {
//                            p.color = Color.parseColor("#D32F2F")
//                            val background = RectF(
//                                itemView.right.toFloat() + dX,
//                                itemView.top.toFloat(),
//                                itemView.right.toFloat(),
//                                itemView.bottom.toFloat()
//                            )
//                            c.drawRect(background, p)
//                            icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
//                            val icon_dest = RectF(
//                                itemView.right.toFloat() - 2 * width,
//                                itemView.top.toFloat() + width,
//                                itemView.right.toFloat() - width,
//                                itemView.bottom.toFloat() - width
//                            )
//                            c.drawBitmap(icon, null, icon_dest, p)
//                        }
//                    }
//                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                }
//            }
//        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//        itemTouchHelper.attachToRecyclerView(history_recyclerview)
//    }

}
