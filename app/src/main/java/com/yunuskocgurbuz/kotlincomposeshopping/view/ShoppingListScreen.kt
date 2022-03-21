package com.yunuskocgurbuz.kotlincomposeshopping.view

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.yunuskocgurbuz.kotlincomposeshopping.R
import com.yunuskocgurbuz.kotlincomposeshopping.entity.ShoppingEntity
import com.yunuskocgurbuz.kotlincomposeshopping.model.Category
import com.yunuskocgurbuz.kotlincomposeshopping.viewmodel.ShoppingListViewModel
import com.yunuskocgurbuz.kotlincomposeshopping.viewmodel.ShoppingSQLiteViewModel
import com.yunuskocgurbuz.kotlincomposeshopping.viewmodel.ShoppingViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


var categoryName: String = "PurpleCat"

@Composable
fun ShoppingListScreen(navController: NavController) {

    val poppinsBold = Font(R.font.poppins_bold)

    //for SQLite connection
    val context = LocalContext.current
    val shoppingSqliteViewModel: ShoppingSQLiteViewModel = viewModel(
        factory = ShoppingViewModelFactory(context.applicationContext as Application)
    )

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Column {

            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Categories",
                fontFamily = FontFamily(poppinsBold),

                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()

            )
            Divider(
                color = Color.Black,
                modifier = Modifier.fillMaxWidth().width(1.dp)
            )

            ShoppingList(navController, shoppingSqliteViewModel)

        }

        dragShoppingCart(navController, shoppingSqliteViewModel)

    }

}




@Composable
fun ShoppingList(navController: NavController, shoppingSqliteViewModel: ShoppingSQLiteViewModel, viewModel: ShoppingListViewModel = hiltViewModel()){

    val shoppingList by remember { viewModel.shoppingList }
    val errorMessage by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (errorMessage.isNotEmpty()) {
            RetryViewMessage(error = errorMessage) {
            }
        }
    }

    Column() {
        LoadCategoryList(navController, shoppingList, shoppingSqliteViewModel)
    }

}


@Composable
fun CategoryItemRow(itemName: String, shoppingViewModel: ShoppingSQLiteViewModel, backgColor: String ){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                val date = SimpleDateFormat("dd.MM.yyyy HH:mm")
                val ShoppingDate: String = date.format(Date())

                val insertShoppingDate = ShoppingEntity(itemName, 1, ShoppingDate)

                shoppingViewModel.AddShopping(insertShoppingDate)

            },
        backgroundColor = Color(android.graphics.Color.parseColor(backgColor)),
        elevation = 10.dp,
        shape = RoundedCornerShape(5.dp)
    ) {

        Text(text = itemName,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(15.dp)
        )
    }
}

@Composable
fun LoadCategoryList(navController: NavController, categoryList: List<Category>, shoppingSqliteViewModel: ShoppingSQLiteViewModel) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        var refreshing by remember { mutableStateOf(false) }
        LaunchedEffect(refreshing) {
            if (refreshing) {
               // delay(100)
                refreshing = false
            }
        }


        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = { refreshing = true }
        ) {

            Column() {

                LazyRow(contentPadding = PaddingValues(0.dp)
                ) {

                    items(categoryList) { categoryList ->
                        val poppinsLight = Font(R.font.poppins_light)
                        Column(
                            horizontalAlignment =  Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(30.dp))
                                .wrapContentWidth()
                                .background( if(categoryList.name == categoryName)  Color.LightGray else Color.White)
                                .padding(end = 15.dp, start = 15.dp, top = 10.dp, bottom = 10.dp)
                                .clickable() {
                                    categoryName = categoryList.name
                                    refreshing = true

                                }

                        ) {

                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = categoryList.name,
                                fontFamily = FontFamily(poppinsLight),
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )


                        }
                    }

                }



                for(items in categoryList) {
                    if (!items.items.isNullOrEmpty()) {
                        if (items.name.equals(categoryName)) {
                            StaggeredVerticalGrid(
                                numColumns = 2, //put the how many column you want
                                modifier = Modifier.padding(5.dp)
                            ) {
                                for(item in items.items){
                                    CategoryItemRow(item.name, shoppingSqliteViewModel, items.colour)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingCategoryRow(navController: NavController, categoryList: Category) {
    val poppinsLight = Font(R.font.poppins_light)
    Column(
        horizontalAlignment =  Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(30.dp))
            .wrapContentWidth()
            .padding(end = 15.dp, start = 15.dp, top = 10.dp, bottom = 10.dp)
            .clickable() {
                categoryName = categoryList.name
            }

    ) {

        Spacer(modifier = Modifier.width(10.dp))
            Text(
            text = categoryList.name,
            fontFamily = FontFamily(poppinsLight),
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )


    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun dragShoppingCart(
    navController: NavController,
    shoppingSqliteViewModel: ShoppingSQLiteViewModel
){

    val getAllShopping = shoppingSqliteViewModel.readAllShopping.observeAsState(listOf()).value
    val shoppingCount = getAllShopping.size

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {

        val poppinsBold = Font(R.font.poppins_bold)
        val configuration = LocalConfiguration.current

        val screenHeight = configuration.screenHeightDp
        val screenWidth = configuration.screenWidthDp

        var offsetX by remember { mutableStateOf(0) }
        var offsetY by remember { mutableStateOf(0) }



        BadgeBox(
            Modifier
                .offset { IntOffset(offsetX, offsetY) }
                .background(Color.LightGray)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consumeAllChanges()
                        offsetX += dragAmount.x.toInt()
                        offsetY += dragAmount.y.toInt()
                    }
                }
                .clickable {
                    navController.navigate(
                        "shopping_detail_screen"
                    )
                },
            backgroundColor = Color.LightGray,
            badgeContent = {
                Text(
                    text = shoppingCount.toString(),
                    color = Color.Red,
                    fontFamily = FontFamily(poppinsBold)
                )
            }
        ) {
            Icon(
                Icons.Outlined.ShoppingCart,
                contentDescription = "Cart",
                modifier = Modifier.size(35.dp, 35.dp)
            )
        }

    }
}


@Composable
fun RetryViewMessage(
    error: String,
    onRetry: () -> Unit
) {
    Column() {
        Spacer(modifier = Modifier.height(10.dp))
        Text("Ops! Something went wrong.", color = Color.Red, fontSize = 20.sp)
    }
}