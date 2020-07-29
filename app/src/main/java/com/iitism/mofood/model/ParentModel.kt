package com.iitism.mofood.model

import org.json.JSONArray

data class ParentModel (
    val orderId:String,
    val restaurantName:String,
    val totalCost:String,
    val orderPlacedAt:String,
    val childArray: JSONArray
)