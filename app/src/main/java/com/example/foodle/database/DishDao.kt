package com.example.foodle.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {

    @Insert
    suspend fun insertDishDetails(dishEntity: DishEntity)

    @Query("SELECT * FROM dishesTable ORDER BY id")
    fun getAllDishes() : Flow<List<DishEntity>>

    @Delete
    suspend fun deleteDish(dishEntity: DishEntity)

    @Update
    suspend fun updateDishDetails(dishEntity: DishEntity)

    @Query("SELECT * FROM dishesTable WHERE type=:filterType")
    fun getFilteredDishesList(filterType:String) : Flow<List<DishEntity>>

    @Query("SELECT * FROM dishesTable WHERE favoriteDish==1")
    fun getFavDishes(): Flow<List<DishEntity>>
}