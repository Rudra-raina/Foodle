package com.example.foodle.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class DishRepository(private val dishDao : DishDao) {

    @WorkerThread
    suspend fun insertDish(dishEntity: DishEntity){
        dishDao.insertDishDetails(dishEntity)
    }

    val allDishesList : Flow<List<DishEntity>> = dishDao.getAllDishes()

    @WorkerThread
    suspend fun deletedish(dishEntity: DishEntity){
        dishDao.deleteDish(dishEntity)
    }

    @WorkerThread
    suspend fun updateDish(dishEntity: DishEntity){
        dishDao.updateDishDetails(dishEntity)
    }

    fun filteredListDish(value:String) : Flow<List<DishEntity>> = dishDao.getFilteredDishesList(value)

    val favoriteDishes : Flow<List<DishEntity>> = dishDao.getFavDishes()
}