package com.mihir.team360.admin_tracker.employeeDates

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihir.team360.mvvm.models.LocationModel
import com.mihir.team360.mvvm.models.Resource
import com.mihir.team360.mvvm.repository.Repository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DatesViewModel(val repository: Repository) : ViewModel() {

    fun getLocationDate(user_id:String) : LiveData<Resource<List<String>>> {
        viewModelScope.launch(IO) {
            repository.getLocationDate(user_id)
        }
        return repository.dateList
    }
}