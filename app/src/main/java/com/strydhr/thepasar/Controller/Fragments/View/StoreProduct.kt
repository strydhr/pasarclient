package com.strydhr.thepasar.Controller.Fragments.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.strydhr.thepasar.Adapters.ProductAdapter
import com.strydhr.thepasar.Model.ProductDocument
import com.strydhr.thepasar.Model.StoreDocument

import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.StoreServices
import kotlinx.android.synthetic.main.fragment_store_product.*

/**
 * A simple [Fragment] subclass.
 */
class StoreProduct : Fragment() {

    lateinit var storeDoc:StoreDocument
    lateinit var adapter:ProductAdapter
    var productList: MutableList<ProductDocument> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_store_product, container, false)
        val storeStr = arguments?.getString("store")
        storeDoc = Gson().fromJson(storeStr,StoreDocument::class.java)


        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        StoreServices.listStoreProduct(storeDoc){
            productList = it
            println(it.size)
            adapter = ProductAdapter(context!!.applicationContext,productList){
                var objStr = Gson().toJson(it)

                val bundle = Bundle()
                bundle.putString("product", objStr)
                val fragInfo = AddProduct()
                fragInfo.arguments = bundle


                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragInfo).addToBackStack(null)
                    .commit()


            }
            viewproduct_recyclerview.adapter = adapter
            val layoutManager = LinearLayoutManager(context!!.applicationContext)
            viewproduct_recyclerview.layoutManager = layoutManager
            viewproduct_recyclerview.setHasFixedSize(true)
        }
    }

}
