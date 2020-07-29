package com.iitism.mofood.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {

    @Insert
    fun insert(restaurantEntity: RestaurantEntity)

    @Delete
    fun delete(restaurantEntity: RestaurantEntity)

    @Query("SELECT * FROM Restaurants")
    fun getAllRestaurant():List<RestaurantEntity>

    @Query("SELECT * FROM Restaurants WHERE restaurant_id= :restId")
    fun getRestaurantById(restId:String):RestaurantEntity


}