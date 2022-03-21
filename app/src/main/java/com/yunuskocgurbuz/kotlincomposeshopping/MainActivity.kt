package com.yunuskocgurbuz.kotlincomposeshopping

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yunuskocgurbuz.kotlincomposeshopping.ui.theme.KotlinComposeShoppingTheme
import com.yunuskocgurbuz.kotlincomposeshopping.view.ShoppingDetailScreen
import com.yunuskocgurbuz.kotlincomposeshopping.view.ShoppingListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinComposeShoppingTheme {

                val navController = rememberNavController()
                NavHost(navController =  navController, startDestination = "shopping_list_screen"){
                    composable("shopping_list_screen"){

                        ShoppingListScreen(navController)

                        val activity = (LocalContext.current as? Activity)
                        BackHandler(true) {
                            activity?.finish()
                        }

                    }

                    composable("shopping_detail_screen"){

                        ShoppingDetailScreen()

                    }


                }
            }
        }
    }
}
