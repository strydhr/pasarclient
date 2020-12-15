package com.strydhr.thepasar.Controller.Fragments.View

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import com.strydhr.thepasar.Adapters.ProductAdapter
import com.strydhr.thepasar.Model.ProductDocument
import com.strydhr.thepasar.Model.StoreDocument
import com.strydhr.thepasar.Model.itemPurchasing
import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.StoreServices
import kotlinx.android.synthetic.main.fragment_store_product.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class StoreProduct : Fragment() {

    lateinit var storeDoc:StoreDocument
    lateinit var adapter:ProductAdapter
    var productList: MutableList<ProductDocument> = ArrayList()
    var cart: MutableList<itemPurchasing> = ArrayList()
    var hasDeliveryTime = false
    lateinit var dateStr:String

    lateinit var proceedBtn:Button

    lateinit var mAdView : AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_store_product, container, false)
        proceedBtn = rootView.findViewById(R.id.store_product_checkoutbtn) as Button

        val storeStr = arguments?.getString("store")
        storeDoc = Gson().fromJson(storeStr, StoreDocument::class.java)


            proceedBtn.setOnClickListener {
                if (cart.size > 0) {
                    var objStr = Gson().toJson(cart)
//            var dateStr = Gson().toJson()
                    var storeStr = Gson().toJson(storeDoc)
                    println(objStr)

                    val bundle = Bundle()
                    bundle.putString("items", objStr)
                    bundle.putString("date", dateStr)
                    bundle.putString("store", storeStr)
                    bundle.putBoolean("theresDeliveryTime", hasDeliveryTime)
                    val fragInfo = Cart()
                    fragInfo.arguments = bundle
                    fragInfo.setTargetFragment(this, 2)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragInfo).addToBackStack(null)
                        .commit()
                }

        }


        val adRequest = AdRequest.Builder()
            .build()

        val adView = rootView.findViewById(R.id.adView) as AdView
        adView.loadAd(adRequest)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




        StoreServices.listStoreProduct(storeDoc){
            productList = it
            println(it.size)
            adapter = ProductAdapter(context!!.applicationContext, productList){
                var objStr = Gson().toJson(it)
                val cartStr = Gson().toJson(cart)
//                val bundle = Bundle()
//                bundle.putString("product", objStr)
//                val fragInfo = AddProduct()
//                fragInfo.arguments = bundle
//
//
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, fragInfo).addToBackStack(null)
//                    .commit()

                var addProductPopup = Intent(
                    context!!.applicationContext,
                    PopupAddProduct::class.java
                )
                addProductPopup.putExtra("product", objStr)
//                addProductPopup.putExtra("cart",cartStr)
                startActivityForResult(addProductPopup, 1)


            }
            viewproduct_recyclerview.adapter = adapter
            val layoutManager = LinearLayoutManager(context!!.applicationContext)
            viewproduct_recyclerview.layoutManager = layoutManager
            viewproduct_recyclerview.setHasFixedSize(true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val itemStr = data?.getStringExtra("item")
                val item = Gson().fromJson<itemPurchasing>(itemStr, itemPurchasing::class.java)
                dateStr = data?.getStringExtra("date")!!
                val formatter = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm", Locale.ENGLISH)
//                date = formatter.parse(dateStr)
                hasDeliveryTime = data?.getBooleanExtra("hasDeliverytime", false)!!

                cart.add(item)


            }
        }else if (requestCode == 2){
            cart.clear()
        }
        else if (requestCode == 3){
            if (resultCode == Activity.RESULT_OK){
                var itemStr = data?.getStringExtra("cartUpdated")
                val array = Gson().fromJson<Array<itemPurchasing>>(itemStr, Array<itemPurchasing>::class.java)
                cart = ArrayList(array.toMutableList())

            }
        }
    }





}
