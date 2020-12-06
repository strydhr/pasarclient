package com.strydhr.thepasar.Controller.Fragments.View

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.strydhr.thepasar.R
import kotlinx.android.synthetic.main.popup_update_radius.*

class PopupUpdateRadius: AppCompatActivity() {

    var selectedRadius:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_update_radius)

        enableProgressBar(false)

        // Create an ArrayAdapter
        val adapter1 = ArrayAdapter.createFromResource(this,
            R.array.radius_type, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        radius_spinner.adapter = adapter1
        radius_spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) { // First item will be gray
                if (position == 0){
                    radius_hint.text = "Distance"
                }else{
                    radius_hint.text = ""
                    (parent?.getChildAt(0) as TextView).setTextColor(Color.parseColor("#68727A"))
                    (parent?.getChildAt(0) as TextView).setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.spinnertext))
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                radius_hint.text = "Distance"
            }
        })


    }
    fun confirmBtnClicked(view: View){
        errorHandler(radius_spinner.selectedItem.toString())
    }

    private fun errorHandler(radius:String){
        if (radius.isEmpty()){
            Toast.makeText(this,"Please select a distance", Toast.LENGTH_LONG).show()
        }else {
            val str = radius.split(" ")
            enableProgressBar(true)
            val fromRadiusUpdatePopup = Intent()
            fromRadiusUpdatePopup.putExtra("newRadius",str[0])
            setResult(Activity.RESULT_OK,fromRadiusUpdatePopup)
            finish()
        }

    }

    fun enableProgressBar(enable: Boolean){
        if (enable){

            radius_progressbar.visibility = View.VISIBLE
            radius_saveBtn.isEnabled = false
        }else{
            radius_progressbar.visibility = View.INVISIBLE
            radius_saveBtn.isEnabled = true
        }
    }
}