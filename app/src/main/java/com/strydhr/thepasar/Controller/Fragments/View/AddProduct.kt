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
    var itemleft:Int = 0
    var counter:Int = 1

    lateinit var breakfastBtn:Button
    lateinit var lunchBtn:Button
    lateinit var dinnerBtn:Button
    lateinit var minusBtn:Button
    lateinit var addBtn:Button
    lateinit var spinner:Spinner
    lateinit var checkoutBtn:Button



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
        minusBtn = rootView.findViewById(R.id.addproduct_minusbtn) as Button
        addBtn = rootView.findViewById(R.id.addproduct_addbtn) as Button

        checkoutBtn = rootView.findViewById(R.id.addproduct_addtocartbtn) as Button


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

            if(productDoc.product!!.count == 1){
                addBtn.isEnabled = false
                addBtn.setTextColor(resources.getColor(R.color.grey))

            }

        }

        minusBtn.isEnabled = false
        minusBtn.setTextColor(resources.getColor(R.color.grey))


        breakfastBtn.setOnClickListener {
            breakfastBtnClicked()
        }

        lunchBtn.setOnClickListener {
            lunchBtnClicked()
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

        minusBtn.setOnClickListener {
            counter = counter - 1
            addproduct_counter.text = "$counter"
            val price = productDoc.product?.price
            val total = price?.times(counter)
            addproduct_total_label.text = "RM " + String.format("%.2f", total)
            if (counter == 1){
                minusBtn.isEnabled = false
                minusBtn.setTextColor(resources.getColor(R.color.grey))
            }
            if (productDoc.product!!.type == "Handmade") {
                if (counter < productDoc.product!!.count!!) {
                    addBtn.isEnabled = true
                    addBtn.setTextColor(resources.getColor(R.color.blueBtn))
                }
            }
        }

        addBtn.setOnClickListener {
            counter = counter + 1
            addproduct_counter.text = "$counter"
            val price = productDoc.product?.price
            val total = price?.times(counter)
            addproduct_total_label.text = "RM " + String.format("%.2f", total)
            if (counter == 2){
                minusBtn.isEnabled = true
                minusBtn.setTextColor(resources.getColor(R.color.blueBtn))
            }
            if (productDoc.product!!.type == "Handmade") {
                if (counter == productDoc.product!!.count) {
                    addBtn.isEnabled = false
                    addBtn.setTextColor(resources.getColor(R.color.grey))
                }
            }
        }

        checkoutBtn.setOnClickListener {
            
        }


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

        addproduct_total_label.text = "RM " + String.format("%.2f", productDoc.product?.price)
        addproduct_counter.text = "1"


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

    fun breakfastBtnClicked(){
        for (i in 7 .. 10){
            spinnerArray.add("${i}:00")
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!.applicationContext, android.R.layout.simple_spinner_item, spinnerArray
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner.adapter = adapter
    }

    fun lunchBtnClicked(){
        for (i in 11 .. 14){
            spinnerArray.add("${i}:00")
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!.applicationContext, android.R.layout.simple_spinner_item, spinnerArray
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner.adapter = adapter
    }

    fun dinnerBtnClicked(){
        for (i in 17 .. 20){
            spinnerArray.add("${i}:00")
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!.applicationContext, android.R.layout.simple_spinner_item, spinnerArray
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner.adapter = adapter
    }


}
