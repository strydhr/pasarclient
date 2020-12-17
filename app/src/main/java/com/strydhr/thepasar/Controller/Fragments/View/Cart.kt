package com.strydhr.thepasar.Controller.Fragments.View

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.provider.MediaStore.Video
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.gson.Gson
import com.strydhr.thepasar.Adapters.CartAdapter
import com.strydhr.thepasar.Model.Order
import com.strydhr.thepasar.Model.StoreDocument
import com.strydhr.thepasar.Model.itemPurchasing
import com.strydhr.thepasar.R
import com.strydhr.thepasar.Services.NotificationServices
import com.strydhr.thepasar.Services.PurchaseServices
import com.strydhr.thepasar.Utilities.userGlobal
import kotlinx.android.synthetic.main.fragment_cart.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class Cart : Fragment() {

    lateinit var store:StoreDocument
    lateinit var cartList: ArrayList<itemPurchasing>
    var hasDeliveryTime = false
    lateinit var adapter:CartAdapter
    lateinit var date:Date
    lateinit var checkoutBtn:Button

    private val p = Paint()
    private lateinit var mInterstitialAd: InterstitialAd

    var hourComponent:Int = 0
    var spinnerArray: ArrayList<String> = ArrayList()

    var totalOders = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_cart, container, false)
        checkoutBtn = rootView.findViewById(R.id.cart_checkoutbtn)as Button

        val itemStr = arguments?.getString("items")
        val dateStr = arguments?.getString("date")
        val storeStr = arguments?.getString("store")
//        cartList = Gson().fromJson(itemStr, List<itemPurchasing>::class.java)
        val gson = Gson()
        val array = gson.fromJson<Array<itemPurchasing>>(itemStr, Array<itemPurchasing>::class.java)
        cartList = ArrayList(array.toMutableList())
        store = Gson().fromJson(storeStr, StoreDocument::class.java)
        val formatter = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm", Locale.ENGLISH)
        date = formatter.parse(dateStr)

        updateCheckoutBtn()
        checkoutBtn.setOnClickListener {
            val stockItems = cartList.filter { it.hasDeliveryTime == false }
            val readyItems = cartList.filter { it.hasDeliveryTime == true }

            var address = ""
            if (!userGlobal?.unitNumber.isNullOrEmpty()){
                address = userGlobal?.unitNumber + ", " + userGlobal?.address
            }else{
                address = userGlobal?.address!!
            }

            for (item in cartList){
                val total = item.itemCount
                totalOders += total!!
            }
            println(store.store?.deviceToken)
            NotificationServices.sendNotification(context!!.applicationContext,
                store.store?.deviceToken!!,"New Meal order","Ordered $totalOders items")


            if (mInterstitialAd.isLoaded){
                mInterstitialAd.show()
                if (stockItems.size > 0){
                    val order = Order(
                        stockItems,
                        Date(),
                        false,
                        date,
                        userGlobal?.uid,
                        userGlobal?.name,
                        address,
                        userGlobal!!.l,
                        userGlobal?.phone,
                        userGlobal?.deviceToken,
                        store.documentId,
                        store.store?.name,
                        store.store?.ownerId,
                        false,
                        1,
                        ""
                    )
                    PurchaseServices.confirmPurchase(order)
                }

                if (readyItems.size > 0){
                    val order = Order(
                        readyItems,
                        Date(),
                        true,
                        date,
                        userGlobal?.uid,
                        userGlobal?.name,
                        address,
                        userGlobal!!.l,
                        userGlobal?.phone,
                        userGlobal?.deviceToken,
                        store.documentId,
                        store.store?.name,
                        store.store?.ownerId,
                        false,
                        1,
                        ""
                    )
                    PurchaseServices.confirmPurchase(order)
                }

                exit()
            }else{
                if (stockItems.size > 0){
                    val order = Order(
                        stockItems,
                        Date(),
                        false,
                        date,
                        userGlobal?.uid,
                        userGlobal?.name,
                        address,
                        userGlobal!!.l,
                        userGlobal?.phone,
                        userGlobal?.deviceToken,
                        store.documentId,
                        store.store?.name,
                        store.store?.ownerId,
                        false,
                        1,
                        ""
                    )
                    PurchaseServices.confirmPurchase(order)
                }

                if (readyItems.size > 0){
                    val order = Order(
                        readyItems,
                        Date(),
                        true,
                        date,
                        userGlobal?.uid,
                        userGlobal?.name,
                        address,
                        userGlobal!!.l,
                        userGlobal?.phone,
                        userGlobal?.deviceToken,
                        store.documentId,
                        store.store?.name,
                        store.store?.ownerId,
                        false,
                        1,
                        ""
                    )
                    PurchaseServices.confirmPurchase(order)
                }

                exit()
            }

        }


        mInterstitialAd = InterstitialAd(context!!.applicationContext)
//        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"   Testadid
        mInterstitialAd.adUnitId = "ca-app-pub-1330351136644118/3147641003"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = CartAdapter(context!!.applicationContext, cartList)
        cart_recyclerview.adapter = adapter
        val layoutManager = LinearLayoutManager(context!!.applicationContext)
        cart_recyclerview.layoutManager = layoutManager
        cart_recyclerview.setHasFixedSize(true)
        enableSwipe()
    }


    private fun exit(){
        val intent = Intent(context, StoreProduct::class.java)
        intent.putExtra("doneCheckout", true)
        targetFragment?.onActivityResult(2,RESULT_OK, intent)
        fragmentManager?.popBackStack()
    }

    private fun updatedCart(){
        val intent = Intent(context, StoreProduct::class.java)
        var objStr = Gson().toJson(cartList)
        intent.putExtra("cartUpdated", objStr)
        targetFragment?.onActivityResult(3,RESULT_OK, intent)
//        fragmentManager?.popBackStack()
    }

    private fun updateCheckoutBtn(){
        var total = 0.0
        for (item in cartList){
            val price = item.itemCount!!.toDouble() * item.productPrice!!
            total += price

        }
        checkoutBtn.setText("Confirm - Total RM ${(String.format("%.2f",total))}")
    }

    private fun enableSwipe() {
        val simpleItemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition


                    var removedItem = adapter!!.removeItem(position)
                    cartList = cartList?.filter { it != removedItem } as ArrayList<itemPurchasing>
//                    var itemToDelete = removedItem.content
//                    var list = realm.where(RealmChecklist::class.java).equalTo("content",itemToDelete).findFirst()
//                    realm.beginTransaction()
//                    list?.deleteFromRealm()
//                    realm.commitTransaction()
//                    adapter.notifyDataSetChanged()


                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    val icon: Bitmap
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                        val itemView = viewHolder.itemView
                        val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                        val width = height / 3

                        if (dX > 0) {
                            p.color = Color.parseColor("#388E3C")
                            val background =
                                RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                            c.drawRect(background, p)
                            icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
                            val icon_dest = RectF(
                                itemView.left.toFloat() + width,
                                itemView.top.toFloat() + width,
                                itemView.left.toFloat() + 2 * width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, icon_dest, p)
                        } else {
                            p.color = Color.parseColor("#D32F2F")
                            val background = RectF(
                                itemView.right.toFloat() + dX,
                                itemView.top.toFloat(),
                                itemView.right.toFloat(),
                                itemView.bottom.toFloat()
                            )
                            c.drawRect(background, p)
                            icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
                            val icon_dest = RectF(
                                itemView.right.toFloat() - 2 * width,
                                itemView.top.toFloat() + width,
                                itemView.right.toFloat() - width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, icon_dest, p)
                        }
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(cart_recyclerview)
    }

    override fun onDetach() {
        super.onDetach()
        println("NONDIDI")
        updatedCart()
//        val sharedPreference =  this.getActivity()?.getSharedPreferences("pref", Context.MODE_PRIVATE);
//        var editor = sharedPreference?.edit()
//        editor?.putString("username","Anupam")
//        editor?.putLong("l",100L)
//        editor?.commit()
    }

}
