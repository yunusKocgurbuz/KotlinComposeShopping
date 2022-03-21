package com.yunuskocgurbuz.kotlincomposeshopping.dependencyinjection

import com.yunuskocgurbuz.kotlincomposecurrencyexchanger.util.Constants.BASE_URL
import com.yunuskocgurbuz.kotlincomposeshopping.repository.ShoppingAPIRepository
import com.yunuskocgurbuz.kotlincomposeshopping.service.ShoppingAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.google.gson.GsonBuilder

import com.google.gson.Gson




@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideShoppingRepository(
        api: ShoppingAPI
    ) = ShoppingAPIRepository(api)

    @Singleton
    @Provides
    fun provideShoppingApi(): ShoppingAPI {

        var okHttpClient: OkHttpClient? = null
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .build()
            .create(ShoppingAPI::class.java)
    }


}