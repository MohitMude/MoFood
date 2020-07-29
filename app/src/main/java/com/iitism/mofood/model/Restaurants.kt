package com.iitism.mofood.model

data class Restaurants (
    val restauarantId: String,
    val restauarantName: String,
    val restauarantRating: String,
    val restauarantCost: String,
    val restauarantImage: String
) {
    fun getName():String{
        return restauarantName
    }
}