package com.example.foodle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DishEntity::class],version = 1)
abstract class DishRoomDB : RoomDatabase() {

    abstract fun dishDao() : DishDao

    companion object{
        @Volatile
        private  var INSTANCE : DishRoomDB? = null

        fun getDatabase(context: Context) : DishRoomDB{
            return INSTANCE ?: synchronized(this){

                val instance= Room.databaseBuilder(context.applicationContext, DishRoomDB::class.java, "DishDatabase").build()
                INSTANCE=instance
                instance
            }

        }
    }
}