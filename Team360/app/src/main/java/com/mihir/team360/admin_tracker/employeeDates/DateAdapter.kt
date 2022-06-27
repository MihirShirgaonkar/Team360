package com.mihir.team360.admin_tracker.employeeDates

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mihir.team360.LocationView.LocationActivity
import com.mihir.team360.R
import com.mihir.team360.mvvm.models.LocationModel

class DateAdapter(val context: Context,var list: List<String>) : RecyclerView.Adapter<DateAdapter.DAViewModel>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DAViewModel {
        val view = LayoutInflater.from(context).inflate(R.layout.date_card,parent,false)
        return DAViewModel(view)
    }

    override fun onBindViewHolder(holder: DAViewModel, position: Int) {
        val i = list.get(position)
holder.dateTv.setText(i.toString())

        holder.itemView.setOnClickListener {
            val intent = Intent(context,LocationActivity::class.java)
            intent.putExtra("date",i)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }


    class DAViewModel(itemView: View) : RecyclerView.ViewHolder(itemView){
val dateTv = itemView.findViewById<TextView>(R.id.date)
    }
}