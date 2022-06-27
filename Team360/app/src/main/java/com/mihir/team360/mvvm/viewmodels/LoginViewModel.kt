package com.mihir.team360.mvvm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mihir.team360.mvvm.models.AdminModel
import com.mihir.team360.mvvm.models.Resource
import com.mihir.team360.mvvm.models.UserModel
import com.mihir.team360.mvvm.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call

class LoginViewModel(val repository: Repository) : ViewModel() {


    fun verifyAdmin(username: String, password: String): Call<List<AdminModel>> {
        return repository.verifyAdmin(username, password)
    }

    fun verifyUser(username: String, password: String): Call<List<UserModel>> {
        return repository.verifyUser(username, password)
    }


}