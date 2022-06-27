package com.mihir.team360.gps_service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mihir.team360.mvvm.repository.Repository

class GPSViewModelFactory(val repository: Repository):ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GPSViewModel(repository) as T
    }
}