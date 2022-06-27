package com.mihir.team360.admin_tracker.employeeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihir.team360.mvvm.models.Resource
import com.mihir.team360.mvvm.models.UserModel
import com.mihir.team360.mvvm.repository.Repository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class EmployessListViewModel(val repository: Repository) : ViewModel() {

    fun getEmployee(admin_id : String) : LiveData<Resource<List<UserModel>>>{
        viewModelScope.launch(IO) {
        repository.getEmployee(admin_id)
        }
        return repository.mutableGetEmployes
    }
}