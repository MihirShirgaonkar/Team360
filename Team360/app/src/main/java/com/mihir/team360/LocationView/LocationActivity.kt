package com.mihir.team360.LocationView

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import android.content.SharedPreferences
import android.os.Bundle
import com.mihir.team360.R
import com.mihir.team360.LocationView.LocationActivity
import android.os.Build
import android.view.WindowManager
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.single.PermissionListener
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.PermissionToken
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.karumi.dexter.listener.PermissionRequest
import com.mihir.team360.CustomProgressDialogue
import com.mihir.team360.SplashScreenActivity
import com.mihir.team360.appliaction.Team360Application
import com.mihir.team360.mvvm.models.LocationModel
import com.mihir.team360.mvvm.models.Resource
import java.util.ArrayList

class LocationActivity : AppCompatActivity() {
    private var progressDialogue: CustomProgressDialogue? = null
    var smf: SupportMapFragment? = null
    var client: FusedLocationProviderClient? = null
    var gMap: GoogleMap? = null
                                    var dataList: List<LocationModel>? =null
    var latLngList: MutableList<LatLng> = ArrayList()
    lateinit var locationViewModel: LocationViewModel
    var markerList: List<Marker> = ArrayList()
    private val registrationPreferences by lazy {
        getSharedPreferences(
            SplashScreenActivity.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    var user_id : String? = null
    private var registerationPrefsEditor: SharedPreferences.Editor? = null

    var progressBar : CustomProgressDialogue?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        progressDialogue= CustomProgressDialogue(this)
        dataList = ArrayList()


        val repo = (application as Team360Application).repository
        locationViewModel=ViewModelProvider(this,LocationViewModelFactory(repo)).get(LocationViewModel::class.java)


        user_id=registrationPreferences!!.getString("User_Id","").toString()
        val name =registrationPreferences!!.getString("User_Name","").toString()

        findViewById<TextView>(R.id.name).setText(name)

        registerationPrefsEditor = registrationPreferences.edit()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decor = window.decorView
            if (true) {
                decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.systemUiVisibility = 0
            }
        }
        smf = supportFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment?
        client = LocationServices.getFusedLocationProviderClient(this)
        Dexter.withContext(applicationContext)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    myLocation
                }

                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}
                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).check()
    }//
    //                        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Your Current Location..!!");
//
//                        googleMap.addMarker(markerOptions);
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
//
    //                        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                            @Override
//                            public void onMapClick(LatLng latLng) {
//                                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng);
//                                    Marker marker = gMap.addMarker(markerOptions1);
//
//                                    latLngList.add(latLng);
//                                    markerList.add(marker);
//
//                            }
//                        });

    //                        if (polyline!=null) polyline
    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    private val myLocation: Unit
        private get() {
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
            val task = client!!.lastLocation
            task.addOnSuccessListener {
                smf!!.getMapAsync { googleMap -> //
//                    val latLng2 = LatLng(18.0791999, 73.4290858)
//                                MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Your Current Location..!!");
//                        googleMap.addMarker(markerOptions);
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
//
                    gMap = googleMap
                    //                        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                            @Override
//                            public void onMapClick(LatLng latLng) {
//                                    MarkerOptions markerOptions1 = new MarkerOptions().position(latLng);
//                                    Marker marker = gMap.addMarker(markerOptions1);
//
//                                    latLngList.add(latLng);
//                                    markerList.add(marker);
//
//                            }
//                        });

//                        if (polyline!=null) polyline


                    locationViewModel.getLocationLive(user_id!!,intent.getStringExtra("date").toString()).observe(this,
                        Observer {
                            when(it){
                                is Resource.Loading ->{

                                    progressDialogue!!.show()
                                }
                                is Resource.Success ->{
//                                    dataList = it.data!!
                                    latLngList.clear()
                                    gMap!!.clear()
                                    gMap=googleMap


                                    it.data!!.forEach { i ->
                                        val latLng1 = LatLng(i.Latitude as Double, i.Longitude as Double)
                    latLngList.add(latLng1)

                                    }

                                    val polylineOptions = PolylineOptions()
                                        .addAll(latLngList).clickable(true)
                                    var polyline: Polyline = gMap!!.addPolyline(polylineOptions)
                                    progressDialogue!!.dismiss()



                                    val markerOptions= MarkerOptions().position(latLngList.get(0)).title("Your Current Location..!!");
                                    gMap!!.addMarker(markerOptions);
                                    gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngList.get(0),17f));
                                }
                                is Resource.Error ->{

Toast.makeText(this,it.errorMessage,Toast.LENGTH_LONG).show()
                                    progressDialogue!!.dismiss()
                                }
                            }
                        })


//                    latLngList.add(latLng2)

                }
            }
        }

    fun back(view: View?) {
        finish()
    }

    companion object {
        const val MyPREFERENCES = "MyPrefs"
    }
}