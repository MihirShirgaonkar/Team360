package com.mihir.team360.admin_tracker.employeeDates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mihir.team360.mvvm.repository.Repository

class DatesViewModelFactory(val repository: Repository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DatesViewModel(repository) as T
    }
}