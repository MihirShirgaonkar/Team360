package com.mihir.team360.addemployee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mihir.team360.mvvm.repository.Repository

class AddEmployeeViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEmployeeViewModel(repository) as T
    }
}