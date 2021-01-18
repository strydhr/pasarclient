package com.strydhr.thepasar.Controller.Fragments.View

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.strydhr.thepasar.Model.ProductDocument
import com.strydhr.thepasar.Model.itemPurchasing
import com.strydhr.thepasar.R
import com.takusemba.spotlight.OnSpotlightListener
import com.takusemba.spotlight.OnTargetListener
import com.takusemba.spotlight.Spotlight
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.shape.Circle
import com.takusemba.spotlight.shape.RoundedRectangle
import kotlinx.android.synthetic.main.popup_addproduct.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PopupAddProduct: AppCompatActivity()  {

    var hintCount = 0

    lateinit var productDoc: ProductDocument
    var cart:ArrayList<itemPurchasing> = ArrayList()
    var itemleft:Int = 0
    var counter:Int = 1

    lateinit var breakfastBtn: Button
    lateinit var lunchBtn: Button
    lateinit var dinnerBtn: Button
    lateinit var minusBtn: Button
    lateinit var addBtn: Button
    lateinit var spinner: Spinner
    lateinit var checkoutBtn: Button



    var hourComponent:Int = 0
    var spinnerArray: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_addproduct)
        var productStr = intent.getStringExtra("product")
        productDoc = Gson().fromJson<ProductDocument>(productStr,ProductDocument::class.java)

        //Hint
        if (!restorePrefData()){
            if (productDoc.product?.type != "Homemade"){
                val displayMetrics = DisplayMetrics()
                this?.getWindowManager()?.getDefaultDisplay()!!.getMetrics(displayMetrics)
                val height = displayMetrics.heightPixels
                val width = displayMetrics.widthPixels

                println(height)
                var firstHintHeight = 0f
                var firstrowHeight = 0f
                var secondrowHeight = 0f
                var secondHintHeight = 0f
                var textHeight1 = 0
                var textHeight2 = 0
                if (height > 2300){
                    firstrowHeight = 600f
                    firstHintHeight = 1550f
                    secondrowHeight = 170f
                    secondHintHeight = 1920f
                    textHeight1 = 700
                    textHeight2 = 1100

                }else if(height > 2000){
                    firstrowHeight =  400f
                    firstHintHeight = 1150f
                    secondrowHeight = 110f
                    secondHintHeight = 1420f
                    textHeight1 = 700
                    textHeight2 = 1100

                }else if (height > 1100){
                    firstrowHeight =  320f
                    firstHintHeight = 910f
                    secondrowHeight = 100f
                    secondHintHeight = 1090f
                    textHeight1 = 500
                    textHeight2 = 800
                }else if (height > 700){
                    firstrowHeight =  160f
                    firstHintHeight = 450f
                    secondrowHeight = 60f
                    secondHintHeight = 560f
                    textHeight1 = 300
                    textHeight2 = 600
                }


                val targets = ArrayList<Target>()

                // first target
                val firstRoot = FrameLayout(this!!)
                val first = layoutInflater.inflate(R.layout.layout_hints, firstRoot)
                val firstTarget = com.takusemba.spotlight.Target.Builder()
                    .setAnchor(width.toFloat() / 2, firstHintHeight)
                    .setShape(RoundedRectangle(firstrowHeight, width.toFloat(), 5f))
                    .setOverlay(first)
                    .setOnTargetListener(object : OnTargetListener {
                        override fun onStarted() {
                            val text = first?.findViewById<TextView>(R.id.custom_text)
                            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)

                            params.setMargins(10, textHeight1, 10, 10)
                            text?.layoutParams = params
                            text!!.setText("Choose when you want you meals ready")
                        }

                        override fun onEnded() {

                        }
                    })
                    .build()

                targets.add(firstTarget)
                val secondTarget = com.takusemba.spotlight.Target.Builder()
                    .setAnchor(width.toFloat() / 2, secondHintHeight)
                    .setShape(RoundedRectangle(secondrowHeight, width.toFloat(), 5f))
                    .setOverlay(first)
                    .setOnTargetListener(object : OnTargetListener {
                        override fun onStarted() {
                            val text = first?.findViewById<TextView>(R.id.custom_text)
                            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)

                            params.setMargins(10, textHeight2, 10, 10)
                            text?.layoutParams = params
                            text!!.setText("Choose exact time on when should we deliver your meals and then tap the add to cart button below")
                        }

                        override fun onEnded() {

                        }
                    })
                    .build()

                targets.add(secondTarget)

                val spotlight = Spotlight.Builder(this!!)
                    .setTargets(targets)
                    .setBackgroundColorRes(R.color.blackOpacity)
                    .setDuration(1000L)
                    .setAnimation(DecelerateInterpolator(2f))
                    .setOnSpotlightListener(object : OnSpotlightListener {
                        override fun onStarted() {

                        }

                        override fun onEnded() {


                        }
                    })
                    .build()

                spotlight.start()
                first.setOnClickListener {
                    hintCount += 1
                    println(hintCount)
                    if (hintCount == 2){
                        println(hintCount)
                        savePrefsData()
                        spotlight.finish()
                    }else{
                        spotlight.next()
                    }

                }
            }
        }
        //End of hints

//        val cartStr = intent.getStringExtra("cart")
//        cart = Gson().fromJson<ArrayList<itemPurchasing>>(cartStr,ArrayList<itemPurchasing>::class.java)

        breakfastBtn = findViewById(R.id.addproduct_breakfastbtn)
        lunchBtn = findViewById(R.id.addproduct_lunchbtn)
        dinnerBtn = findViewById(R.id.addproduct_dinnerbtn)
        spinner = findViewById(R.id.addproduct_spinner)
        minusBtn = findViewById(R.id.addproduct_minusbtn)
        addBtn = findViewById(R.id.addproduct_addbtn)

        checkoutBtn = findViewById(R.id.addproduct_addtocartbtn)
        val timeContainer = findViewById<RelativeLayout>(R.id.addproduct_timecontainer)
        val timeSpinner = findViewById<RelativeLayout>(R.id.addproduct_time)
        val timeSeparator = findViewById<RelativeLayout>(R.id.addproduct_timecontainer_separator)
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
            errorHandler()
        }

        setProductDetails()
        setTiming()

    }

    fun setProductDetails(){
        Glide.with(this).load(productDoc.product?.profileImage).into(
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
            breakfastBtn.setBackgroundColor(breakfastBtn.context.resources.getColor(R.color.blueBtn))
        }else{
            breakfastBtn.isEnabled = false
            breakfastBtn.setBackgroundColor(breakfastBtn.context.resources.getColor(R.color.grey))
        }

        if((6 .. 12).contains(hour)){
            println("lunch")
            lunchBtn.isEnabled = true
            lunchBtn.setBackgroundColor(lunchBtn.context.resources.getColor(R.color.blueBtn))
        }else{
            lunchBtn.isEnabled = false
            lunchBtn.setBackgroundColor(lunchBtn.context.resources.getColor(R.color.grey))
        }

        if((6 .. 19).contains(hour)){
            println("dinner")
            dinnerBtn.isEnabled = true
            dinnerBtn.setBackgroundColor(dinnerBtn.context.resources.getColor(R.color.blueBtn))
        }else{
            dinnerBtn.isEnabled = false
            dinnerBtn.setBackgroundColor(dinnerBtn.context.resources.getColor(R.color.grey))
        }

        //Dinner time
//        if(gmt )
    }

    fun breakfastBtnClicked(){
        spinnerArray.clear()
        for (i in 7 .. 10){
            spinnerArray.add("${i}:00")
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, spinnerArray
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner.adapter = adapter
    }

    fun lunchBtnClicked(){
        spinnerArray.clear()
        for (i in 11 .. 14){
            spinnerArray.add("${i}:00")
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, spinnerArray
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner.adapter = adapter
    }

    fun dinnerBtnClicked(){
        spinnerArray.clear()
        for (i in 17 .. 20){
            spinnerArray.add("${i}:00")
        }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_item, spinnerArray
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        spinner.adapter = adapter
    }

    private fun errorHandler(){
        if(productDoc.product!!.type != "Handmade"){
            if(spinner.selectedItem.toString().isEmpty()){
                Toast.makeText(this,"Please select delivery time", Toast.LENGTH_LONG).show()
            }else{
                val item = itemPurchasing(productDoc.documentId,productDoc?.product!!.name,productDoc?.product!!.price,counter,true,productDoc?.product!!.colorClass)
                val deliveryTime = dateFinder(spinner.selectedItem.toString())
                val fromAddToCart = Intent()
                fromAddToCart.putExtra("item",Gson().toJson(item))
                fromAddToCart.putExtra("date",deliveryTime)
                fromAddToCart.putExtra("hasDeliverytime",true)
                setResult(Activity.RESULT_OK,fromAddToCart)
                finish()
            }
        }else{
            val item = itemPurchasing(productDoc.documentId,productDoc?.product!!.name,productDoc?.product!!.price,counter,false,productDoc?.product!!.colorClass)
            val deliveryTime = dateFinder("12:00")
            val fromAddToCart = Intent()
            fromAddToCart.putExtra("item",Gson().toJson(item))
            fromAddToCart.putExtra("date",deliveryTime)
            fromAddToCart.putExtra("hasDeliverytime",false)
            setResult(Activity.RESULT_OK,fromAddToCart)
            finish()
        }


    }

    fun dateFinder(time:String):String{
        val dateformatter = SimpleDateFormat("yyyy-MM-dd")
        val datehalf = dateformatter.format(Date())
        val formatter = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm", Locale.ENGLISH)
        val dateStr = datehalf + " at " + time
//        val date = formatter.parse(dateStr)

        return dateStr

    }

    // Initials hints
    private fun savePrefsData() {
        val pref = this.getSharedPreferences(
            "MyPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putBoolean("seenAddProductHint", true)
        editor.commit()
    }


    private fun restorePrefData(): Boolean {
        val pref = this.getSharedPreferences(
            "MyPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        return pref.getBoolean("seenAddProductHint", false)
    }

}