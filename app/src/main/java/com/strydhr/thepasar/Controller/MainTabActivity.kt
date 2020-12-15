package com.strydhr.thepasar.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.strydhr.thepasar.Controller.Fragments.History
import com.strydhr.thepasar.Controller.Fragments.HomeMain
import com.strydhr.thepasar.Controller.Fragments.Order
import com.strydhr.thepasar.Controller.Fragments.Profile
import com.strydhr.thepasar.R

class MainTabActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.action_home -> {
                replaceFragment(HomeMain())
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_order -> {
                replaceFragment(Order())
                return@OnNavigationItemSelectedListener true
            }
            R.id.action_history -> {
                replaceFragment(History())
                return@OnNavigationItemSelectedListener true
            }

            R.id.action_profile -> {
                replaceFragment(Profile())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_tab)
        //Navigation Bar
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val menu: Menu = bottomNavigation.menu
        val menuItem: MenuItem = menu.getItem(0)
        menuItem.isChecked = true
        replaceFragment(HomeMain())
        bottomNavigation.itemIconTintList = null
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        println("donut")
        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
            println("halo")
            //additional code
        } else {
            println("where are we")
            supportFragmentManager.popBackStack()
        }
    }
    override fun onSupportNavigateUp(): Boolean { //This method is called when the up button is pressed. Just the pop back stack.
        supportFragmentManager.popBackStack()
        return true
    }
}
