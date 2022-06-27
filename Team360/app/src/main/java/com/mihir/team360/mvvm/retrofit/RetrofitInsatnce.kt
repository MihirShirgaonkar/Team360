package com.mihir.team360.mvvm.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInsatnce {

    private const val BASE_URL = "http://team360server-001-site1.ctempurl.com/"

    @Volatile
    private var INSTANCE : Retrofit?  =  null

     fun getInstance(): Retrofit {
         if (INSTANCE==null){
             synchronized(this){
                 INSTANCE=Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
             }
         }
         return INSTANCE!!
     }
}