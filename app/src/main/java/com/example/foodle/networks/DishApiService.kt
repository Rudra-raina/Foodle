package com.example.foodle.networks

import com.example.foodle.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class DishApiService {

    private val api=Retrofit.Builder().baseUrl(Constants.spoonacularBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(DishInterface::class.java)

    fun getSearchedDishFromInternet(type:String) : Single<FilteredDishes.DishesFromAPI> {
        return api.getDishes(Constants.spoonacularAPiKeyValue,false,type,1)
    }

    fun getRandomDishFromInternet() : Single<FilteredDishes.DishesFromAPI>{
        return api.getDishes(Constants.spoonacularAPiKeyValue,false,"whole30",1)
    }
}