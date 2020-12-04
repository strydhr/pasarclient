package com.strydhr.thepasar.Model

import com.google.firebase.firestore.ServerTimestamp
import java.util.*
import kotlin.collections.ArrayList

data class Product(
    var uid:String? = null,
    var type:String? = null,
    var name:String? = null,
    var details:String? = null,
    var sid:String? = null,
    var count:Int? = null,
    var price:Double? = null,
    var profileImage:String? = null,
    @field:JvmField var availability:Boolean? = null,
    @field:JvmField var hasCounter:Boolean? = null,
    var colorClass:String? = null


)

data class ProductDocument(
    var documentId:String? = null,
    var product:Product? = null
)

data class itemPurchasing(
    var productId:String? = null,
    var productName:String? = null,
    var productPrice:Double? = null,
    var itemCount:Int? = null,
    @field:JvmField var hasDeliveryTime:Boolean? = null,
    var colorClass:String? = null


)

data class Order(
    var items:ArrayList<itemPurchasing>? = null,
    @ServerTimestamp var date: Date? = null,
    @field:JvmField var hasDeliveryTime:Boolean? = null,
    @ServerTimestamp var deliveryTime: Date? = null,
    var purchaserId:String? = null,
    var purchaserName:String? = null,
    var purchaserAddress:String? = null,
    var purchaserCoor:ArrayList<Double>? = null,
    var purchaserPhone:String? = null,
    var purchaserDeviceToken:String? = null,
    var storeId:String? = null,
    var storeName:String? = null,
    var ownerId:String? = null,
    @field:JvmField var hasDelivered:Boolean? = null,
    var confirmationStatus:Int? = null,
    var comment:String? = null


)
data class OrderDocument(
    var documentId:String? = null,
    var order:Order? = null
)

data class Receipts(
    var items:ArrayList<itemPurchasing>? = null,
    @ServerTimestamp var date: Date? = null,
    @field:JvmField var hasDeliveryTime:Boolean? = null,
    @ServerTimestamp var deliveryTime: Date? = null,
    var purchaserId:String? = null,
    var purchaserName:String? = null,
    var purchaserAddress:String? = null,
    var purchaserPhone:String? = null,
    var purchaserDeviceToken:String? = null,
    var storeId:String? = null,
    var storeName:String? = null,
    var ownerId:String? = null,
    @field:JvmField var hasDelivered:Boolean? = null,
    @field:JvmField var caseClosed:Boolean? = null,
    var orderId:String? = null


)
data class ReceiptDocument(
    var documentId:String? = null,
    var receipt:Receipts? = null
)

data class Complaint(
    var items:ArrayList<itemPurchasing>? = null,
    @ServerTimestamp var date: Date? = null,
    @ServerTimestamp var deliveryTime: Date? = null,
    var purchaserId:String? = null,
    var purchaserName:String? = null,
    var purchaserAddress:String? = null,
    var storeId:String? = null,
    var storeName:String? = null,
    var ownerId:String? = null,
    var receiptId:String? = null,
    var complaint:String? = null


)