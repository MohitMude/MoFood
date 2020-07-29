package com.iitism.mofood.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="Restaurants")
data class RestaurantEntity (
    @PrimaryKey
    @ColumnInfo(name="restaurant_id")
      val restId:String,
    @ColumnInfo(name="restaurant_name")
      val restName:String,
    @ColumnInfo(name="restaurant_rating")
     val restRating:String,
    @ColumnInfo(name="cost")
      val cost:String,
    @ColumnInfo(name="image")
      val imgUrl:String
)