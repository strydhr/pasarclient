package com.strydhr.thepasar.Controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.strydhr.thepasar.Adapters.IntroViewPagerAdapter
import com.strydhr.thepasar.Model.ScreenItem
import com.strydhr.thepasar.R


class IntroActivity : AppCompatActivity() {

    private lateinit var screenPager: ViewPager
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    lateinit var tabIndicator: TabLayout
    lateinit var btnNext: Button
    var position = 0
    var btnGetStarted: Button? = null
    var btnAnim: Animation? = null
    var tvSkip: TextView? = null
    val mList: MutableList<ScreenItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intro)

        //init
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        tabIndicator = findViewById(R.id.tabLayout)
        btnNext = findViewById(R.id.intro_button)

        // fill list screen

        mList.add(
            ScreenItem(
                "Having a craving?",
                "Order home cooked food right to your door step from neighbours and nearby communities.",
                R.drawable.hint1
            )
        )
        mList.add(
            ScreenItem(
                "Select a nearby kitchen",
                "Satisfy that craving with selections from out easy to navigate food categories",
                R.drawable.hint2
            )
        )
        mList.add(
            ScreenItem(
                "Picky about what you eat",
                "Find out more about the ingredients in your food, so you know exactly what you're ordering",
                R.drawable.hint3
            )
        )
        mList.add(
            ScreenItem(
                "Stay notified",
                "Receive real time notification about the status and delivery of your order",
                R.drawable.hint4
            )
        )

        // setup viewpager

        // setup viewpager
        screenPager = findViewById(R.id.intro_pager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager.adapter = introViewPagerAdapter
        // setup tablayout with viewpager

        tabIndicator?.setupWithViewPager(screenPager);

        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if (position != 3) {
                    btnNext?.setText("Next")
                } else {
                    btnNext?.setText("Dismiss")
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
    }

    fun nextBtnClicked(view: View){
        position = screenPager.currentItem
        if (position < mList.size) {

            position++;
            screenPager.currentItem = position;


        }
        if (position == 3){
            btnNext?.setText("Dismiss")
        }else if (position == 4){
            savePrefsData()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            btnNext?.setText("Next")
        }



    }

    private fun savePrefsData() {
        val pref = applicationContext.getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("hasIntroOpened", true)
        editor.commit()
    }
}
