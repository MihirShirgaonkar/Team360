package com.mihir.team360.gps_service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.mihir.team360.R
import com.mihir.team360.SplashScreenActivity
import com.mihir.team360.appliaction.Team360Application
import com.mihir.team360.mvvm.models.LocationModel
import com.mihir.team360.mvvm.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LocationService : Service() {
    private val NOTIFICATION_CHANNEL_ID = "my_notification_location"
    private val TAG = "LocationService"

    private val registrationPreferences by lazy {
        getSharedPreferences(
            SplashScreenActivity.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
    }
    private var registerationPrefsEditor: SharedPreferences.Editor? = null
    var repo: Repository? = null


    override fun onCreate() {
        super.onCreate()
        isServiceStarted = true
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_baseline_my_location_24)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
            startForeground(1, builder.build())
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        registerationPrefsEditor = registrationPreferences.edit()

        repo = (application as Team360Application).repository

        val timer = Timer()
        LocationHelper().startListeningUserLocation(
            this, object : MyLocationListener {
                override fun onLocationChanged(location: Location?) {
                    mLocation = location
                    mLocation?.let {
                        AppExecutors.instance?.networkIO()?.execute {


                                    Log.d(TAG, "onLocationChanged: Latitude ${it.latitude} , Longitude ${it.longitude}")
                                    Log.d(TAG, "run: Running = Location Update Successful")

                            var locationModel = LocationModel(
                                "",
                                "",
                                registrationPreferences.getString("Admin_Id", "").toString(),
                                it.latitude,
                                "",
                                it.longitude,
                                "NULL",
                                "NULL",
                                registrationPreferences.getString("User_Id", "").toString()
                            )

                            repo!!.markLocation(locationModel).enqueue(object :
                                Callback<Map<String, String>> {
                                override fun onResponse(
                                    call: Call<Map<String, String>>,
                                    response: Response<Map<String, String>>
                                ) {
//on respone success
                                }

                                override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
// on response failure
                                }

                            })



                        }
                    }
                }
            })
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
        super.onDestroy()
        isServiceStarted = false


    }

    companion object {
        var mLocation: Location? = null
        var isServiceStarted = false
    }


}