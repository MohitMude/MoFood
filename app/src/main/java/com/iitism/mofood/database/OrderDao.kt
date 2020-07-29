package com.iitism.mofood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {

    @Insert
    fun insert(orderEntity: OrderEntity)

    @Delete
    fun delete(orderEntity: OrderEntity)

    @Query("SELECT * FROM `Order`")
    fun getAllOrders():List<OrderEntity>

    @Query("SELECT * FROM `Order` WHERE food_id= :foodItemId")
    fun getOrderById(foodItemId:String):OrderEntity

    @Query("SELECT food_cost FROM `Order`")
    fun getCost():List<String>

    @Query("SELECT food_id FROM `Order`")
    fun getId():List<String>

    @Query("DELETE FROM `Order`")
    fun delete()
}