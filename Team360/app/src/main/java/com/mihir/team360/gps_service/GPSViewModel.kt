package com.mihir.team360.gps_service

import androidx.lifecycle.ViewModel
import com.mihir.team360.mvvm.models.LocationModel
import com.mihir.team360.mvvm.repository.Repository
import retrofit2.Call

class GPSViewModel(val repository: Repository) : ViewModel() {

    fun markLocation(loactionModel : LocationModel): Call<Map<String, String>>{
        return repository.markLocation(loactionModel)
    }
}