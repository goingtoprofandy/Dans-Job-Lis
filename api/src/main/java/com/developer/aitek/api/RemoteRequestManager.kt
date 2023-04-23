package com.developer.aitek.api

import android.annotation.SuppressLint
import android.content.Context
import com.developer.aitek.api.data.*
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface RemoteRequestManager {

    @GET("recruitment/positions.json")
    suspend fun getLists(
        @Query("page") page: Int,
        @Query("description") query: String,
        @Query("location") location: String,
        @Query("full_time") is_full_time: Boolean
    ): Response<MutableList<ItemJob>>

    @GET("recruitment/positions/{id}")
    suspend fun getDetail(
        @Path("id") id: String,
    ): Response<ItemJob>

    companion object{
        @SuppressLint("HardwareIds")
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            context: Context
        ) : RemoteRequestManager {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(ChuckInterceptor(context))
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://dev3.dansmultipro.co.id/api/")
                .client(okHttpClient)
                .build()
                .create(RemoteRequestManager::class.java)
        }
    }
}