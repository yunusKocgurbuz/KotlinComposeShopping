package com.yunuskocgurbuz.kotlincomposeshopping.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunuskocgurbuz.kotlincomposeimageapp.util.Resource
import com.yunuskocgurbuz.kotlincomposeshopping.model.Category
import com.yunuskocgurbuz.kotlincomposeshopping.repository.ShoppingAPIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor (
    private val repository: ShoppingAPIRepository
): ViewModel() {

    var shoppingList = mutableStateOf<List<Category>>(listOf())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    init {
        loadShopping()

    }

    fun loadShopping(){
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getShoppingList()

            when(result){
                is Resource.Success -> {
                    val newItems = result.data!!.categories

                    errorMessage.value = ""
                    isLoading.value = false
                    shoppingList.value += newItems

                }

                is Resource.Error -> {

                    errorMessage.value = result.message!!
                    isLoading.value = false

                }
            }

        }
    }

}