package com.example.foodle.database

import android.app.Application
import com.example.foodle.database.DishRepository
import com.example.foodle.database.DishRoomDB

class DishApplication : Application(){

    val database by lazy { DishRoomDB.getDatabase(this)}
    val repository by lazy { DishRepository(database.dishDao()) }


}