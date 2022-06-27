package com.mihir.team360.admin_tracker.employeeList

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.mihir.team360.R
import com.mihir.team360.SplashScreenActivity
import com.mihir.team360.admin_tracker.employeeDates.EmpoyesDatesActivity
import com.mihir.team360.mvvm.models.UserModel

class EmployeeListAdapter(val context: Context, var list:  List<UserModel>) : RecyclerView.Adapter<EmployeeListAdapter.ELViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ELViewHolder {
val view  =LayoutInflater.from(context).inflate(R.layout.employee_card,parent,false)
        return ELViewHolder(view)
    }

    override fun onBindViewHolder(holder: ELViewHolder, position: Int) {
        val i = list.get(position)
        holder.nameTv.setText(i.User_Name)
        holder.idTv.setText("User ID : "+i.User_Id+"\n Phone: ${i.Phone.toString()} \n Email : ${i.Email.toString()} ")



        holder.callIv.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + i.Phone.toString())
            context.startActivity(dialIntent)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context,EmpoyesDatesActivity::class.java)
            intent.putExtra("User_Id",i.User_Id)
             val registrationPreferences by lazy {context.getSharedPreferences(SplashScreenActivity.MyPREFERENCES, Context.MODE_PRIVATE)}
             var registerationPrefsEditor: SharedPreferences.Editor? = null
            registerationPrefsEditor = registrationPreferences.edit()
            registerationPrefsEditor.putString("User_Id",i.User_Id)
            registerationPrefsEditor.putString("User_Name",i.User_Name)
            registerationPrefsEditor.commit()

            context.startActivity(intent)
        }



    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ELViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
val nameTv = itemView.findViewById<TextView>(R.id.name)
val idTv = itemView.findViewById<TextView>(R.id.id)
val callIv = itemView.findViewById<ImageView>(R.id.call)
    }
}