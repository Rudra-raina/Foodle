package com.example.foodle.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "dishesTable")
data class DishEntity(
    @ColumnInfo val image : String,
    @ColumnInfo val imageSource : String,
    @ColumnInfo val title : String,
    @ColumnInfo val type : String,
    @ColumnInfo val category : String,
    @ColumnInfo val ingredients : String,
    @ColumnInfo val CookingTime : String,
    @ColumnInfo val DirectionsToCook : String,
    @ColumnInfo var favoriteDish : Boolean = false,
    @PrimaryKey(autoGenerate = true) val id : Int
) : Parcelable
