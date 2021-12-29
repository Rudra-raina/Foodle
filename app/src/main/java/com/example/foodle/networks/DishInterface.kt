package com.example.foodle.networks

import com.example.foodle.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface DishInterface {

    @GET(Constants.spoonacularEndPoint)
    fun getDishes(
        @Query(Constants.apiKey) apiKey : String,
        @Query(Constants.limitLicense) limitLicense : Boolean,
        @Query(Constants.tags) tags : String,
        @Query(Constants.number) number : Int
    ) : Single<FilteredDishes.DishesFromAPI>
}