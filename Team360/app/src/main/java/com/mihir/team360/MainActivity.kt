package com.mihir.team360

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.mihir.team360.admin_tracker.employeeDates.EmpoyesDatesActivity
import com.mihir.team360.databinding.ActivityMainBinding
import com.mihir.team360.gps_service.LocationService2
import com.mihir.team360.login.LoginActivity
import com.mihir.team360.mvvm.models.UserModel

class MainActivity : AppCompatActivity() {

lateinit var binding : ActivityMainBinding

    private val registrationPreferences by lazy {getSharedPreferences(SplashScreenActivity.MyPREFERENCES, Context.MODE_PRIVATE)}
    private var registerationPrefsEditor: SharedPreferences.Editor? = null

    var LOCATION_REQUEST_CODE = 10001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        registerationPrefsEditor = registrationPreferences.edit()

        val userData = UserModel(
            registrationPreferences!!.getString("Added_On","").toString(),
            registrationPreferences!!.getString("Admin_Id","").toString(),
            registrationPreferences!!.getString("Admin_Name","").toString(),
            registrationPreferences!!.getString("Email","").toString(),
            registrationPreferences!!.getString("Password","").toString(),
            registrationPreferences!!.getString("Phone","").toString(),
            registrationPreferences!!.getString("Remark","").toString(),
            registrationPreferences!!.getString("Remark_","").toString(),
            registrationPreferences!!.getString("User_Id","").toString(),
            registrationPreferences!!.getString("User_Name","").toString(),
            registrationPreferences!!.getString("Username","").toString(),

        )
binding.userData=userData
        binding.address.setText("Address : "+userData.Remark_)
        binding.email.setText("Email : "+userData.Email)
        binding.phone.setText("Phone : "+userData.Phone)
        binding.id.setText("Employee ID : "+userData.User_Id)

        binding.tracker.setOnClickListener {
            val intent = Intent(this, EmpoyesDatesActivity::class.java)
            intent.putExtra("User_Id",userData.User_Id.toString())
            startActivity(intent)
        }


        binding.logout.setOnClickListener {

            val dialog1 = Dialog(this@MainActivity)
            dialog1.setContentView(R.layout.dialog_main_)
            dialog1.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog1.setCancelable(false)
            dialog1.getWindow()!!.setBackgroundDrawable(getDrawable(R.drawable.popup_bg_trans))

            val cancle: TextView = dialog1.findViewById(R.id.btn_cancel)
            val submit: TextView = dialog1.findViewById(R.id.btn_submit)


            submit.setOnClickListener {
                val serviceIntent = Intent(this, LocationService2::class.java)
                stopService(serviceIntent)
                binding.textOfGps.setText("Location Tracking : Off")

                registrationPreferences.edit().clear().apply()
                val i = Intent(this@MainActivity, LoginActivity::class.java)

                finish()
                startActivity(i)
                dialog1.dismiss()

            }


            cancle.setOnClickListener {

                dialog1.dismiss()

            }
            dialog1.show()
        }


        binding.switchGps.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                // The switch enabled
                    onStartService()
                binding.textOfGps.setText("Location Tracking : On")


            } else {
                // The switch disabled
                val serviceIntent = Intent(this, LocationService2::class.java)
                stopService(serviceIntent)
                binding.textOfGps.setText("Location Tracking : Off")

            }
        })
    }

    fun onStartService() {
        askLocationPermission()
    }

    private fun askLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "1st", Toast.LENGTH_SHORT).show()
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
                Toast.makeText(this, "2st", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "else", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        } else {
            Toast.makeText(this, "2else", Toast.LENGTH_SHORT).show()
            val serviceIntent = Intent(this, LocationService2::class.java)
            startService(serviceIntent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
                //           getLastLoaction();
                val serviceIntent = Intent(this, LocationService2::class.java)
                startService(serviceIntent)
                //                checkSettingsAndStartLocationUpdates();
            } else {
                Toast.makeText(this, "Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}