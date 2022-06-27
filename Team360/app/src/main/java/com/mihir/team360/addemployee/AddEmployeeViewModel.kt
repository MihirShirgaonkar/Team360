package com.mihir.team360.addemployee

import androidx.lifecycle.ViewModel
import com.mihir.team360.mvvm.models.UserModel
import com.mihir.team360.mvvm.repository.Repository
import retrofit2.Call

class AddEmployeeViewModel(val repository: Repository) : ViewModel() {

    fun addEmployee(userModel: UserModel) : Call<Map<String, String>> {
        return repository.addEmployee(userModel)
    }


}