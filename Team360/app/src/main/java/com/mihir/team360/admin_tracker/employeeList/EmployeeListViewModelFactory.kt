package com.mihir.team360.admin_tracker.employeeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.mihir.team360.mvvm.repository.Repository

class EmployeeListViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EmployessListViewModel(repository) as T
    }


}