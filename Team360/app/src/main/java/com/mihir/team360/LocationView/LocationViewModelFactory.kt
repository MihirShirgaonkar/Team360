package com.mihir.team360.LocationView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mihir.team360.mvvm.repository.Repository

class LocationViewModelFactory(val repository: Repository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LocationViewModel(repository) as T
    }
}