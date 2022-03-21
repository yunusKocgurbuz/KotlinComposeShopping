package com.yunuskocgurbuz.kotlincomposeshopping.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.yunuskocgurbuz.kotlincomposeshopping.database.ShoppingDatabase
import com.yunuskocgurbuz.kotlincomposeshopping.entity.ShoppingEntity
import com.yunuskocgurbuz.kotlincomposeshopping.repository.ShoppingSQLiteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingSQLiteViewModel(application: Application): AndroidViewModel(application){

    val readAllShopping: LiveData<List<ShoppingEntity>>

    private val repository: ShoppingSQLiteRepository

    init {
        val ShoppingDao = ShoppingDatabase.getInstance(application).ShoppingDao()
        repository = ShoppingSQLiteRepository(ShoppingDao)
        readAllShopping = repository.readAllShopping
    }


    fun AddShopping(item: ShoppingEntity){
        viewModelScope.launch(Dispatchers.IO ) {
            repository.addShopping(item = item)
        }
    }


}

class ShoppingViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if(modelClass.isAssignableFrom(ShoppingSQLiteViewModel::class.java)){
            return ShoppingSQLiteViewModel(application) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}

