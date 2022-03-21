package com.yunuskocgurbuz.kotlincomposeshopping.view

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yunuskocgurbuz.kotlincomposeshopping.R
import com.yunuskocgurbuz.kotlincomposeshopping.entity.ShoppingEntity
import com.yunuskocgurbuz.kotlincomposeshopping.ui.theme.Purple200
import com.yunuskocgurbuz.kotlincomposeshopping.viewmodel.ShoppingSQLiteViewModel
import com.yunuskocgurbuz.kotlincomposeshopping.viewmodel.ShoppingViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ShoppingDetailScreen() {

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

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "My Orders",
                fontFamily = FontFamily(poppinsBold),

                color = Color.Black,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()

            )
            Divider(
                color = Color.Black,
                modifier = Modifier.fillMaxWidth().width(1.dp)
            )

           MyOrdersList(shoppingSqliteViewModel)
        }

    }

}

@Composable
fun MyOrdersList(shoppingSqliteViewModel: ShoppingSQLiteViewModel) {

    val getAllOrders = shoppingSqliteViewModel.readAllShopping.observeAsState(listOf()).value

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if(getAllOrders.isEmpty()){
            Text(text = "You have not entered any orders yet.", textAlign = TextAlign.Center,
                style = TextStyle(color=Color.DarkGray, fontSize = 16.sp),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize())
        }else {

            StaggeredVerticalGrid(
                numColumns = 1, //put the how many column you want
                modifier = Modifier.padding(2.dp)
            ) {

                for(order in getAllOrders){
                    OrdersListRow(order)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            AlertDialog()
        }

    }

}


@Composable
fun OrdersListRow(ordersList: ShoppingEntity) {
    val poppinsBold = Font(R.font.poppins_bold)
    val poppinsLight = Font(R.font.poppins_light)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        backgroundColor = Color.LightGray,
        elevation = 1.dp,
        shape = RoundedCornerShape(5.dp)
    ) {
        Column {
            Text(text = ordersList.itemName!!, fontFamily = FontFamily(poppinsBold))
            Text(text = ordersList.date.toString(), fontFamily = FontFamily(poppinsLight))
        }
    }

}

@Composable
fun AlertDialog(){
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(false)  }

            Button(onClick = {
                openDialog.value = true
            },
                modifier = Modifier
                    .width(200.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(Color.Cyan)) {
                Text("Order Now")
            }

            if (openDialog.value) {
                val context = LocalContext.current
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Checkout Screen")
                    },
                    text = {
                        Text("Please confirm to complete the purchase.")
                    },
                    confirmButton = {
                        Button(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp)),
                            colors = ButtonDefaults.buttonColors(Color.Cyan),
                            onClick = {
                                openDialog.value = false
                                Toast.makeText(context,"Purchase completed...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }) {
                            Text("Approve")
                        }
                    },
                    dismissButton = {
                        Button(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp)),
                            colors = ButtonDefaults.buttonColors(Color.Cyan),
                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("Dismiss")
                        }
                    }

                )
            }
        }

    }
}
