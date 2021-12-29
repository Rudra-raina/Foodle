package com.example.foodle.networks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class DishApiViewModel : ViewModel(){

    private val dishApiService : DishApiService = DishApiService()
    private val compositeDisposable : CompositeDisposable = CompositeDisposable()

    val loadDish = MutableLiveData<Boolean>()
    val dishResponse = MutableLiveData<FilteredDishes.DishesFromAPI>()
    val dishLoadingError = MutableLiveData<Boolean>()

    fun getSearchedDishesFromInternet(filter:String){
        loadDish.value=true

        compositeDisposable.add(
            dishApiService.getSearchedDishFromInternet(filter)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<FilteredDishes.DishesFromAPI>(){
                    override fun onSuccess(value: FilteredDishes.DishesFromAPI?) {
                        loadDish.value=false
                        dishResponse.value=value!!
                        dishLoadingError.value=false
                    }

                    override fun onError(e: Throwable?) {
                        loadDish.value=false
                        dishLoadingError.value=true
                        e!!.printStackTrace()
                    }

                })
        )
    }

    fun getRandomDishesFromInternet(){
        loadDish.value=true

        compositeDisposable.add(
            dishApiService.getRandomDishFromInternet()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<FilteredDishes.DishesFromAPI>(){
                    override fun onSuccess(value: FilteredDishes.DishesFromAPI?) {
                        loadDish.value=false
                        dishResponse.value=value!!
                        dishLoadingError.value=false
                    }

                    override fun onError(e: Throwable?) {
                        loadDish.value=false
                        dishLoadingError.value=true
                        e!!.printStackTrace()
                    }

                })
        )
    }

}