package com.mihir.team360.admin_tracker.employeeList

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mihir.team360.CustomProgressDialogue
import com.mihir.team360.R
import com.mihir.team360.SplashScreenActivity
import com.mihir.team360.appliaction.Team360Application
import com.mihir.team360.databinding.ActivityEmployeeListBinding
import com.mihir.team360.mvvm.models.Resource
import com.mihir.team360.mvvm.models.UserModel

class EmployeeListActivity : AppCompatActivity() {


    lateinit var elViewModel: EmployessListViewModel
    private val registrationPreferences by lazy {getSharedPreferences(SplashScreenActivity.MyPREFERENCES, Context.MODE_PRIVATE)}
    private var registerationPrefsEditor: SharedPreferences.Editor? = null
var list = ArrayList<UserModel>()
    lateinit var binding : ActivityEmployeeListBinding
    var progressDialogue : CustomProgressDialogue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_employee_list)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        registerationPrefsEditor = registrationPreferences.edit()

        elViewModel=ViewModelProvider(this,EmployeeListViewModelFactory((application as Team360Application).repository)).get(EmployessListViewModel::class.java)
        val adapter = EmployeeListAdapter(this,list)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=adapter

        progressDialogue= CustomProgressDialogue(this)


        elViewModel.getEmployee(registrationPreferences.getString("Admin_Id","").toString())
            .observe(this, Observer {


                when(it){

                    is Resource.Loading ->{
                        adapter.list = ArrayList<UserModel>()
                        adapter.notifyDataSetChanged()
                        progressDialogue!!.show()
                    }
                    is Resource.Success ->{
                        adapter.list= it.data!!
                        adapter.notifyDataSetChanged()
                        progressDialogue!!.dismiss()
                    }
                    is Resource.Error ->{
                        adapter.list = ArrayList<UserModel>()
                        adapter.notifyDataSetChanged()
                        progressDialogue!!.dismiss()
                        Toast.makeText(this,it.errorMessage!!.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            })

    }
}