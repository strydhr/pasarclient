package com.strydhr.thepasar.Controller.Fragments.View

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.strydhr.thepasar.Model.ProductDocument
import com.strydhr.thepasar.R
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class AddProduct : Fragment() {

    lateinit var productDoc:ProductDocument
    lateinit var breakfastBtn:Button
    lateinit var lunchBtn:Button
    lateinit var dinnerBtn:Button
    lateinit var spinner:Spinner
    var hourComponent:Int = 0
    var spinnerArray: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_add_product, container, false)
        val storeStr = arguments?.getString("product")
        productDoc = Gson().fromJson(storeStr, ProductDocument::class.java)

        breakfastBtn = rootView.findViewById(R.id.addproduct_breakfastbtn) as Button
        lunchBtn = rootView.findViewById(R.id.addproduct_lunchbtn) as Button
        dinnerBtn = rootView.findViewById(R.id.addproduct_dinnerbtn) as Button
        spinner = rootView.findViewById(R.id.addproduct_spinner) as Spinner


        val timeContainer = rootView.findViewById(R.id.addproduct_timecontainer) as RelativeLayout
        val timeSpinner = rootView.findViewById(R.id.addproduct_time) as RelativeLayout
        val timeSeparator = rootView.findViewById(R.id.addproduct_timecontainer_separator) as RelativeLayout
        if (productDoc.product?.type == "Handmade"){
            timeContainer.visibility = View.INVISIBLE
            timeContainer.layoutParams.height = 0
            timeSpinner.visibility = View.INVISIBLE
            timeSpinner.layoutParams.height = 0
            timeSeparator.visibility = View.INVISIBLE
            timeSeparator.layoutParams.height = 0
        }

        dinnerBtn.setOnClickListener {
            dinnerBtnClicked()
        }

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) { // First item will be gray
                    addproduct_hint.text = ""
                    (parent?.getChildAt(0) as TextView).setTextColor(Color.parseColor("#68727A"))
                    (parent?.getChildAt(0) as TextView).setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.spinnertext))


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                addproduct_hint.text = "Meal ready by"
            }

        })




        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setProductDetails()
        setTiming()
    }


    fun setProductDetails(){
        Glide.with(context?.applicationContext).load(productDoc.product?.profileImage).into(
            addproduct_image
        )
        addproduct_name.text = productDoc.product?.name
        addproduct_price.text = "RM " + String.format("%.2f", productDoc.product?.price)
        addproduct_description.text = productDoc.product?.details



    }

    fun setTiming(){
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val dt = Date() // current time
        val hours = java.util.Date()
        hourComponent = hour


        if((6 .. 8).contains(hour)){
            breakfastBtn.isEnabled = true
            breakfastBtn.setBackgroundColor(breakfastBtn.context.resources.getColor(R.color.blueBtn));
        }else{
            breakfastBtn.isEnabled = false
            breakfastBtn.setBackgroundColor(breakfastBtn.context.resources.getColor(R.color.grey));
        }

        if((6 .. 12).contains(hour)){
            println("lunch")
            lunchBtn.isEnabled = true
            lunchBtn.setBackgroundColor(lunchBtn.context.resources.getColor(R.color.blueBtn));
        }else{
            lunchBtn.isEnabled = false
            lunchBtn.setBackgroundColor(lunchBtn.context.resources.getColor(R.color.grey));
        }

        if((6 .. 19).contains(hour)){
            println("dinner")
            dinnerBtn.isEnabled = true
            dinnerBtn.setBackgroundColor(dinnerBtn.context.resources.getColor(R.color.blueBtn));
        }else{
           dinnerBtn.isEnabled = false
            dinnerBtn.setBackgroundColor(dinnerBtn.context.resources.getColor(R.color.grey));
        }

        //Dinner time
//        if(gmt )
    }

    fun dinnerBtnClicked(){
        for (i in 17 .. 20){
            spinnerArray.add("${i}:00")
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!.applicationContext, android.R.layout.simple_spinner_item, spinnerArray
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
