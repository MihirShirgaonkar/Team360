package com.mihir.team360.appliaction

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.mihir.team360.mvvm.repository.Repository
import com.mihir.team360.mvvm.retrofit.RetrofitApi
import com.mihir.team360.mvvm.retrofit.RetrofitInsatnce

class Team360Application : Application() {


    companion object{
    public  val CHANNEL_ID = "demoServiceChannel"

    }


    lateinit var repository :Repository

    override fun onCreate() {
        super.onCreate()
        initalize()
        createNotificationChannel()

    }

    private fun initalize() {
        val retrofitApi = RetrofitInsatnce.getInstance().create(RetrofitApi::class.java)
        repository = Repository(retrofitApi,applicationContext  )
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Demo Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }



}