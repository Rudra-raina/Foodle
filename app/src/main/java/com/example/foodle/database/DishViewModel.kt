package com.example.foodle.database

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class DishViewModel(private val repository: DishRepository) : ViewModel() {
    fun insert(dishEntity: DishEntity) = viewModelScope.launch {
        repository.insertDish(dishEntity)
    }

    val allDishesList : LiveData<List<DishEntity>> = repository.allDishesList.asLiveData()

    fun delete(dishEntity: DishEntity) = viewModelScope.launch {
        repository.deletedish(dishEntity)
    }

    fun update(dishEntity: DishEntity) = viewModelScope.launch {
        repository.updateDish(dishEntity)
    }

    fun getFilteredDishes(selection:String) : LiveData<List<DishEntity>> = repository.filteredListDish(selection).asLiveData()

    val favDishes : LiveData<List<DishEntity>> = repository.favoriteDishes.asLiveData()
}

class DishViewModelFactory(private val dishRepository: DishRepository) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(DishViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return DishViewModel(dishRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}