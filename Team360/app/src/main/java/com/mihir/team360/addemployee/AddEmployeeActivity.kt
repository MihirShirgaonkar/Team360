package com.mihir.team360.addemployee

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mihir.team360.CustomProgressDialogue
import com.mihir.team360.R
import com.mihir.team360.SplashScreenActivity
import com.mihir.team360.appliaction.Team360Application
import com.mihir.team360.databinding.ActivityAddEmployeeBinding
import com.mihir.team360.login.LoginActivity
import com.mihir.team360.mvvm.models.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddEmployeeActivity : AppCompatActivity() {

    private val registrationPreferences by lazy {getSharedPreferences(SplashScreenActivity.MyPREFERENCES, Context.MODE_PRIVATE)}
    private var registerationPrefsEditor: SharedPreferences.Editor? = null

    lateinit var addEmployeeViewModel: AddEmployeeViewModel

    lateinit var binding : ActivityAddEmployeeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=DataBindingUtil.setContentView(this,R.layout.activity_add_employee)
        WindowCompat.setDecorFitsSystemWindows(window, false)

    registerationPrefsEditor = registrationPreferences.edit()
        val repo = (application as Team360Application).repository

        addEmployeeViewModel =ViewModelProvider(this,AddEmployeeViewModelFactory(repo)).get(AddEmployeeViewModel::class.java)



        binding.submit.setOnClickListener {

            if (verify(binding.name) && verify(binding.phone) && verify(binding.email) && verify(binding.address)){
                val progressDialogue = CustomProgressDialogue(this@AddEmployeeActivity)
                progressDialogue.show()

                val userModel = UserModel(
                    "",
                    registrationPreferences.getString("Admin_Id","").toString(),
                    registrationPreferences.getString("Name","").toString(),
                    binding.email.text.toString(),
                    binding.name.text.toString().substring(0,2).toString().lowercase()+"2022",
                    binding.phone.text.toString(),
                    binding.address.text.toString(),
                    registrationPreferences.getString("Remark_","").toString(),
                    "",
                    binding.name.text.toString(),
                    binding.phone.text.toString()

                )
                addEmployeeViewModel.addEmployee(userModel).enqueue(object  : Callback<Map<String,String>>{
                    override fun onResponse(
                        call: Call<Map<String, String>>,
                        response: Response<Map<String, String>>
                    ) {
                        if (response.isSuccessful()){
                            if (response.body()!!.get("POST_REQUEST_STATUS").equals("Success")){
                                successDialog(this@AddEmployeeActivity);
                                progressDialogue.dismiss();
                            }else {
                                errorDialog(this@AddEmployeeActivity);
                                progressDialogue.dismiss();
                            }
                        }else {
                            errorDialog(this@AddEmployeeActivity);
                            progressDialogue.dismiss();
                        }
                    }

                    override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                        progressDialogue.dismiss();
                        errorDialog(this@AddEmployeeActivity);
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


    fun successDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.add_user_dialogue)
        dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.getWindow()!!.setBackgroundDrawable(context.getDrawable(R.drawable.popup_bg_trans))
        val cancle: Button = dialog.findViewById(R.id.btn_cancel)
        val submit: Button = dialog.findViewById(R.id.btn_submit)
        cancle.setVisibility(View.GONE)
        submit.setText("OK")
        val textTv: TextView = dialog.findViewById(R.id.text)
        textTv.text = "Employee Added"
        textTv.setTextColor(resources.getColor(R.color.green))
        val logoIv: ImageView = dialog.findViewById(R.id.logo)
        logoIv.setImageResource(R.drawable.checked)
        submit.setOnClickListener{
                dialog.dismiss()
            binding.name.setText("")
            binding.phone.setText("")
            binding.email.setText("")
            binding.address.setText("")

            }

        dialog.show()
    }

    fun errorDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.add_user_dialogue)
        dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(false)
        dialog.getWindow()!!.setBackgroundDrawable(context.getDrawable(R.drawable.popup_bg_trans))
        val cancle: Button = dialog.findViewById(R.id.btn_cancel)
        val submit: Button = dialog.findViewById(R.id.btn_submit)
        cancle.setVisibility(View.GONE)
        submit.setText("OK")
        val textTv: TextView = dialog.findViewById(R.id.text)
        textTv.text = "Failed!!"
        textTv.setTextColor(resources.getColor(R.color.black))
        val logoIv: ImageView = dialog.findViewById(R.id.logo)
        logoIv.setImageResource(R.drawable.rejected)

        submit.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

}