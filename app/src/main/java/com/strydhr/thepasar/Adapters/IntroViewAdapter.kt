package com.strydhr.thepasar.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.strydhr.thepasar.Model.ScreenItem
import com.strydhr.thepasar.R


//class IntroViewAdapter (private val mContext: Context, private  val itemList: MutableList<ScreenItem>) : PagerAdapter() {
//    private var layoutInflater: LayoutInflater? = null
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        layoutInflater = LayoutInflater.from(mContext)
//        val view = layoutInflater!!.inflate(R.layout.layout_screen, container, false)
//        var image: ImageView = view.findViewById(R.id.intro_image)
//        var header: TextView = view.findViewById(R.id.intro_header)
//        var subheader: TextView = view.findViewById(R.id.intro_subheader)
//
//        image.setImageResource(itemList[position].screenImg)
//        header.text = itemList[position].title
//        subheader.text = itemList[position].description
//
//        container.addView(view, position)
//        return view
//    }
//
//    override fun getCount(): Int {
//        return itemList.size
//    }
//
//    override fun isViewFromObject(view: View, obj: Any): Boolean {
//        return view === obj
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        val view = `object` as View
//        container.removeView(view)
//    }
//}

class IntroViewPagerAdapter(var mContext: Context, var mListScreen: List<ScreenItem>) :
    PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen: View = inflater.inflate(R.layout.layout_screen, null)
        val imgSlide = layoutScreen.findViewById<ImageView>(R.id.intro_image)
        val title = layoutScreen.findViewById<TextView>(R.id.intro_header)
        val description = layoutScreen.findViewById<TextView>(R.id.intro_subheader)
        title.text = mListScreen[position].title
        description.text = mListScreen[position].description
        imgSlide.setImageResource(mListScreen[position].screenImg)
        container.addView(layoutScreen)
        return layoutScreen
    }

    override fun getCount(): Int {
        return mListScreen.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
