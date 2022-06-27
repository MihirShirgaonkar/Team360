package com.mihir.team360.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.os.Parcelable
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.mihir.team360.*
import com.mihir.team360.appliaction.Team360Application
import com.mihir.team360.databinding.ActivityLoginBinding
import com.mihir.team360.mvvm.models.AdminModel
import com.mihir.team360.mvvm.models.Resource
import com.mihir.team360.mvvm.models.UserModel
import com.mihir.team360.mvvm.repository.Repository
import com.mihir.team360.mvvm.retrofit.RetrofitApi
import com.mihir.team360.mvvm.retrofit.RetrofitInsatnce
import com.mihir.team360.mvvm.viewmodels.LoginViewModel
import com.mihir.team360.mvvm.viewmodels.LoginViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private val registrationPreferences by lazy {
        getSharedPreferences(
            SplashScreenActivity.MyPREFERENCES,
            Context.MODE_PRIVATE
        )
    }
    private var registerationPrefsEditor: SharedPreferences.Editor? = null

    var progressBar : CustomProgressDialogue?=null

    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        statusBarColor()

        val repo = (application as Team360Application).repository

        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory(repo)).get(LoginViewModel::class.java)

        registerationPrefsEditor = registrationPreferences.edit()

        progressBar= CustomProgressDialogue(this)
        binding.login.setOnClickListener {



            if (verify(binding.p) && verify(binding.pass)){
                progressBar!!.show()

                loginViewModel.verifyUser(binding.phone.toString(), binding.password.toString())
                    .enqueue(object : Callback<List<UserModel>>{
                        override fun onResponse(
                            call: Call<List<UserModel>>,
                            response: Response<List<UserModel>>
                        ) {
                            if (response.isSuccessful){

                                if (response.body()!!.size>0){

                                    val userData :UserModel = response.body()!!.get(0)

                                    registerationPrefsEditor!!.putString("Added_On",userData.Added_On)
                                    registerationPrefsEditor!!.putString("Admin_Id",userData.Admin_Id)
                                    registerationPrefsEditor!!.putString("Admin_Name",userData.Admin_Name)
                                    registerationPrefsEditor!!.putString("Email",userData.Email)
                                    registerationPrefsEditor!!.putString("Password",userData.Password)
                                    registerationPrefsEditor!!.putString("Phone",userData.Phone)
                                    registerationPrefsEditor!!.putString("Remark",userData.Remark)
                                    registerationPrefsEditor!!.putString("Remark_",userData.Remark_)
                                    registerationPrefsEditor!!.putString("User_Id",userData.User_Id)
                                    registerationPrefsEditor!!.putString("User_Name",userData.User_Name)
                                    registerationPrefsEditor!!.putString("Username",userData.Username)
                                    registerationPrefsEditor!!.putString("LoginStatus","1")
                                    registerationPrefsEditor!!.putString("Role","User")
                                    registerationPrefsEditor!!.apply()
                                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                                    finish()
                                    progressBar!!.dismiss()

                                }else{
                                    loginViewModel.verifyAdmin(binding.phone.toString(), binding.password.toString())
                                        .enqueue(object : Callback<List<AdminModel>> {
                                            override fun onResponse(
                                                call: Call<List<AdminModel>>,
                                                response: Response<List<AdminModel>>
                                            ) {
                                                if (response.isSuccessful) {

                                                    if ( response.body()!!.size > 0){

                                                        val adminData :AdminModel= response.body()!!.get(0)
                                                        registerationPrefsEditor!!.putString("Added_On",adminData.Added_On)
                                                        registerationPrefsEditor!!.putString("Admin_Id",adminData.Admin_Id)
                                                        registerationPrefsEditor!!.putString("Email",adminData.Email)
                                                        registerationPrefsEditor!!.putString("Name",adminData.Name)
                                                        registerationPrefsEditor!!.putString("Password",adminData.Password)
                                                        registerationPrefsEditor!!.putString("Phone",adminData.Phone)
                                                        registerationPrefsEditor!!.putString("Remark",adminData.Remark)
                                                        registerationPrefsEditor!!.putString("Username",adminData.Username)
                                                        registerationPrefsEditor!!.putString("Remark_",adminData.Remark_)
                                                        registerationPrefsEditor!!.putString("LoginStatus","1")
                                                        registerationPrefsEditor!!.putString("Role","Admin")

                                                        registerationPrefsEditor!!.apply()


                                                        val intent = Intent(this@LoginActivity, MainActivityAdmin::class.java)
                                                    startActivity(intent)
                                                        finish()
                                                        progressBar!!.dismiss()

                                                    }else{
                                                        Toast.makeText(this@LoginActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
progressBar!!.dismiss()
                                                    }

                                                } else {
                                                    progressBar!!.dismiss()
                                                    Toast.makeText(this@LoginActivity, "Admin Response Failed", Toast.LENGTH_SHORT).show()

                                                }
                                            }

                                            override fun onFailure(call: Call<List<AdminModel>>, t: Throwable) {
                                                Toast.makeText(this@LoginActivity,t.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                                                progressBar!!.dismiss()


                                            }
                                        })
                                }

                            }else{
                                Toast.makeText(this@LoginActivity,"Response Failed",Toast.LENGTH_SHORT).show()
                                progressBar!!.dismiss()
                            }
                        }

                        override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {

                            Toast.makeText(this@LoginActivity,t.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                            progressBar!!.dismiss()


                        }

                    })

            }

        }


    }

    private fun verify(et: EditText): Boolean {
        var value = true
        val s = et.text.toString()
        if (s.isEmpty()) {
            value = false
            et.error = "Error"
        }
        return value
    }

    fun statusBarColor() {
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
                decor.systemUiVisibility = 0
            }
        }
    }



}