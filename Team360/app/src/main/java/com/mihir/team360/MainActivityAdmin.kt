package com.mihir.team360


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.mihir.team360.addemployee.AddEmployeeActivity
import com.mihir.team360.admin_tracker.employeeList.EmployeeListActivity
import com.mihir.team360.databinding.ActivityMainAdminBinding
import com.mihir.team360.login.LoginActivity
import com.mihir.team360.mvvm.models.AdminModel


class   MainActivityAdmin : AppCompatActivity() {

    lateinit var binding: ActivityMainAdminBinding

    private val registrationPreferences by lazy {getSharedPreferences(SplashScreenActivity.MyPREFERENCES, Context.MODE_PRIVATE)}
    private var registerationPrefsEditor: SharedPreferences.Editor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main_admin)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        registerationPrefsEditor = registrationPreferences.edit()

        val adminModel = AdminModel(
            registrationPreferences.getString("Added_On","").toString(),
            registrationPreferences.getString("Admin_Id","").toString(),
            registrationPreferences.getString("Email","").toString(),
            registrationPreferences.getString("Name","").toString(),
            registrationPreferences.getString("Password","").toString(),
            registrationPreferences.getString("Phone","").toString(),
            registrationPreferences.getString("Remark","").toString(),
            registrationPreferences.getString("Remark_","").toString(),
            registrationPreferences.getString("Username","").toString()

        )

        binding.adminData = adminModel

        binding.emailTv.setText("Email : ${adminModel.Email}")
        binding.phoneTv.setText("Phone : +91 ${adminModel.Phone}")
        binding.addressTv.setText("Address : ${adminModel.Remark_}")






        binding.logout.setOnClickListener {

            val dialog1 = Dialog(this@MainActivityAdmin)
            dialog1.setContentView(R.layout.dialog_main_)
            dialog1.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog1.setCancelable(false)
            dialog1.getWindow()!!.setBackgroundDrawable(getDrawable(R.drawable.popup_bg_trans))

            val cancle: TextView = dialog1.findViewById(R.id.btn_cancel)
            val submit: TextView = dialog1.findViewById(R.id.btn_submit)


            submit.setOnClickListener {

                    registrationPreferences.edit().clear().apply()
                    val i = Intent(this@MainActivityAdmin, LoginActivity::class.java)

                    finish()
                    startActivity(i)
                    dialog1.dismiss()

            }


            cancle.setOnClickListener {

                    dialog1.dismiss()

            }
            dialog1.show()


        }


        binding.addEmpolyee.setOnClickListener {
            startActivity(Intent(this,AddEmployeeActivity::class.java))
        }

        binding.tracker.setOnClickListener {
            startActivity(Intent(this,EmployeeListActivity::class.java))

        }

    }
}