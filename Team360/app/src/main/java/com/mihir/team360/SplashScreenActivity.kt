package com.mihir.team360

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import android.os.Bundle
import com.mihir.team360.R
import com.mihir.team360.SplashScreenActivity
import android.view.WindowManager
import android.content.Intent
import android.os.Handler
import com.mihir.team360.MainActivity
import com.mihir.team360.login.LoginActivity

class SplashScreenActivity : AppCompatActivity() {
    private val registrationPreferences by lazy {getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)}
    private var registerationPrefsEditor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        registerationPrefsEditor = registrationPreferences.edit()
        val LoginStatus = registrationPreferences.getString("LoginStatus", "")

        val Role = registrationPreferences.getString("Role", "")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        //This method is used so that your splash activity
        //can cover the entire screen.

        //this will bind your MainActivity.class file with activity_main.
        Handler().postDelayed({
            if (LoginStatus != null && LoginStatus == "1") {
                if (Role == "User") {
                    val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (Role == "Admin") {

                    val intent = Intent(this@SplashScreenActivity, MainActivityAdmin::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            //Intent is used to switch from one activity to another.

            //invoke the SecondActivity.
            //the current activity will get finished.
        }, SPLASH_SCREEN_TIME_OUT.toLong())
    }

    companion object {
        private const val SPLASH_SCREEN_TIME_OUT = 3000
        const val MyPREFERENCES = "MyPrefs"
    }
}