package com.mihir.team360.admin_tracker.employeeDates

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mihir.team360.CustomProgressDialogue
import com.mihir.team360.R
import com.mihir.team360.appliaction.Team360Application
import com.mihir.team360.databinding.ActivityEmpoyesDatesBinding
import com.mihir.team360.mvvm.models.LocationModel
import com.mihir.team360.mvvm.models.Resource

class EmpoyesDatesActivity : AppCompatActivity() {


    lateinit var binding : ActivityEmpoyesDatesBinding

    lateinit var datesViewModel: DatesViewModel
    var progressDialogue : CustomProgressDialogue?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_empoyes_dates)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        progressDialogue= CustomProgressDialogue(this)
        val user_id = intent.getStringExtra("User_Id")
        binding.recyclerView.layoutManager=GridLayoutManager(this, 4)

        val adapter = DateAdapter(this, ArrayList<String>())
        binding.recyclerView.adapter=adapter

        val repo = (application as Team360Application).repository
        datesViewModel =ViewModelProvider(this,DatesViewModelFactory(repo)).get(DatesViewModel::class.java)
//        Toast.makeText(this,user_id.toString(),Toast.LENGTH_SHORT).show()
        datesViewModel.getLocationDate(user_id.toString()).observe(this, Observer {


            when(it){
                 is Resource.Loading ->{
                     adapter.list = ArrayList<String>()
                     progressDialogue!!.show()
                 }
                 is Resource.Success ->{
                     adapter.list = it.data!!.reversed()
                     adapter.notifyDataSetChanged()
                     progressDialogue!!.dismiss()
                 }
                 is Resource.Error ->{
                     adapter.list = ArrayList<String>()
                     adapter.notifyDataSetChanged()

                     progressDialogue!!.dismiss()
                 }
            }
        })







    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}