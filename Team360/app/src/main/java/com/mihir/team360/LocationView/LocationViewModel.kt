package com.mihir.team360.LocationView

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihir.team360.mvvm.models.LocationModel
import com.mihir.team360.mvvm.models.Resource
import com.mihir.team360.mvvm.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel(val repository: Repository) : ViewModel() {

    fun getLocationLive(user_id  :String,date : String) : LiveData<Resource<List<LocationModel>>>{
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLocationDateWise(user_id,date)
        }
        return repository.locationData
    }
}