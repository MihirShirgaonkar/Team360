package com.mihir.team360.gps_service


import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mihir.team360.MainActivity
import com.mihir.team360.R
import com.mihir.team360.SplashScreenActivity
import com.mihir.team360.appliaction.Team360Application
import com.mihir.team360.appliaction.Team360Application.Companion.CHANNEL_ID
import com.mihir.team360.mvvm.models.LocationModel
import com.mihir.team360.mvvm.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class LocationService2 : Service() {

    private val registrationPreferences by lazy {
        getSharedPreferences(
            SplashScreenActivity.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
    }
    private var registerationPrefsEditor: SharedPreferences.Editor? = null
    var repo: Repository? = null

    var LOCATION_REQUEST_CODE = 10001
    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    var locationRequest: LocationRequest? = null
    lateinit var gpsViewModel: GPSViewModel

    override fun onDestroy() {
        stopForeground(true)
        stopSelf()
        super.onDestroy()

    }


    override fun onCreate() {
        super.onCreate()

        val notificationIntent = Intent(this@LocationService2, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this@LocationService2, 0, notificationIntent, 0)


        val notification: Notification =
            NotificationCompat.Builder(this@LocationService2, CHANNEL_ID)
                .setContentTitle("Location Tracking On") //                .setContentText("Latitude : "+ location.getLatitude()+" Longitude :"+ location.getLongitude())
                .setSmallIcon(R.drawable.ic_baseline_my_location_24)
                .setContentIntent(pendingIntent)
                .build()

        startForeground(1, notification)

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        Thread(Runnable {
            registerationPrefsEditor = registrationPreferences.edit()

            repo = (application as Team360Application).repository

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this@LocationService2)
            locationRequest = LocationRequest.create()
            locationRequest!!.interval = 15000
//    locationRequest!!.setFastestInterval(15000)
            locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


            checkSettingsAndStartLocationUpdates()
        }).start()
//        val timer = Timer()
//        LocationHelper().startListeningUserLocation(
//            this, object : MyLocationListener {
//                override fun onLocationChanged(location: Location?) {
//                    mLocation = location
//                    mLocation?.let {
//                    }
//                }
//            })



        return START_STICKY
    }


    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    private fun checkSettingsAndStartLocationUpdates() {
        val request = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!).build()
        val client = LocationServices.getSettingsClient(this)
        val locationSettingsResponseTask = client.checkLocationSettings(request)
        locationSettingsResponseTask.addOnSuccessListener { //Setting of device satisfied
            startLocationUpdates()
        }
        locationSettingsResponseTask.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                val apiException = e
                //                    try {
                //                       apiException.startResolutionForResult(MyService.this, 1001);
                //                    } catch (IntentSender.SendIntentException sendIntentException) {
                //                        sendIntentException.printStackTrace();
                //                    }
            }


            //                Toast.makeText(MainActivity2.this, , Toast.LENGTH_SHORT).show();
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        Thread(Runnable {


        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest!!,
            locationCallback,
            Looper.getMainLooper()
        )
        }).start()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
    }



    var locationCallback: LocationCallback = object : LocationCallback() {


        override fun onLocationResult(locationResult: LocationResult) {


            if (locationResult.equals(null)) {
                return
            }




            for (location in locationResult.locations) {
//                textView.setText(" Longitude :"+ location.getLongitude() +"\nLatitude :"+location.getLatitude());

//                String input = intent.getStringExtra("inputExtra");
//                val db = MyDatabaseHelper(this@MyService)
//                db.addData(location.latitude.toString(), location.longitude.toString(), "NULL")
                var locationModel = LocationModel(
                    "",
                    "",
                    registrationPreferences.getString("Admin_Id", "").toString(),
                    location.latitude,
                    "",
                    location.longitude,
                    "NULL",
                    "NULL",
                    registrationPreferences.getString("User_Id", "").toString()
                )
                repo!!.markLocation(locationModel).enqueue(object : Callback<Map<String, String>> {
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



}