package com.strydhr.thepasar.Controller.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_order.*

/**
 * A simple [Fragment] subclass.
 * try with rejects
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


        listener = OrderServices.realtimeListUpdate(){
            orderList = it
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



//                var objStr = Gson().toJson(it)
//                val cartStr = Gson().toJson(cart)
//                val bundle = Bundle()
//                bundle.putString("product", objStr)
//                val fragInfo = AddProduct()
//                fragInfo.arguments = bundle
//
//
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, fragInfo).addToBackStack(null)
//                    .commit()

//                var addProductPopup = Intent(
//                    context!!.applicationContext,
//                    PopupAddProduct::class.java
//                )
//                addProductPopup.putExtra("product", objStr)
////                addProductPopup.putExtra("cart",cartStr)
//                startActivityForResult(addProductPopup, 1)


                }
                order_recyclerview.adapter = adapter
                val layoutManager = LinearLayoutManager(context!!.applicationContext)
                order_recyclerview.layoutManager = layoutManager
                order_recyclerview.setHasFixedSize(true)
            }

        }


    }

    override fun onStop() {
        super.onStop()
        listener.remove()
        listener2.remove()
    }

}
