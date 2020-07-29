package com.iitism.mofood.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iitism.mofood.model.FoodItem

@Entity(tableName="Order")
data class OrderEntity (
    @PrimaryKey
    @ColumnInfo(name="food_id")
      val foodItemId: String,
    @ColumnInfo(name="food_name")
    val foodName:String,
    @ColumnInfo(name="food_cost")
     val foodCost:String


)