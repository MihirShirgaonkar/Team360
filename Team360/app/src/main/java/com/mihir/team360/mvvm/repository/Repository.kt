package com.mihir.team360.mvvm.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mihir.team360.appliaction.NetworkUtils
import com.mihir.team360.mvvm.models.AdminModel
import com.mihir.team360.mvvm.models.LocationModel
import com.mihir.team360.mvvm.models.Resource
import com.mihir.team360.mvvm.models.UserModel
import com.mihir.team360.mvvm.retrofit.RetrofitApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class Repository(val retrofitApi: RetrofitApi,val context : Context) {


    fun verifyAdmin(username: String, password: String): Call<List<AdminModel>> {
        return retrofitApi.verifyLoginAdmin(username, password)
    }


    fun verifyUser(username: String, password: String): Call<List<UserModel>> {
        return retrofitApi.verifyLoginUser(username, password)
    }

    fun addEmployee(userModel: UserModel) : Call<Map<String, String>> {
        return retrofitApi.addEmployee(userModel.Admin_Id,userModel.Admin_Name,userModel.User_Name,userModel.Phone,userModel.Email,userModel.Username,userModel.Password,userModel.Remark,userModel.Remark_)
    }


    private val mutableGetEmployes_ = MutableLiveData<Resource<List<UserModel>>>()
    val mutableGetEmployes = mutableGetEmployes_ as LiveData<Resource<List<UserModel>>>

     suspend fun getEmployee(admin_id: String){

        if (NetworkUtils.isNetworkAvailable(context)){
            try {
                mutableGetEmployes_.postValue(Resource.Loading())

            val result = retrofitApi.getEmployee(admin_id)
            if (result!!.body()!=null && result.body()!!.size > 0){
                mutableGetEmployes_.postValue(Resource.Success(result.body()))
            }
            }catch (e : Exception){
                mutableGetEmployes_.postValue(Resource.Error(e.localizedMessage.toString()))
            }
        }else{
            mutableGetEmployes_.postValue(Resource.Error("No Internet Connection"))

        }

    }

    private val dateList_ = MutableLiveData<Resource<List<String>>>()
    val dateList = dateList_ as LiveData<Resource<List<String>>>

    suspend fun getLocationDate(user_id : String){

         val data =HashSet<String>()

        if (NetworkUtils.isNetworkAvailable(context)){

            try {
                dateList_.postValue(Resource.Loading())

            val result =  retrofitApi.getLocationDate(user_id)
            if (result!!.body()!=null && result.body()!!.size > 0){
                result.body()!!.forEach {
                    data.add(it.Added_On_Date)
                }
                dateList_.postValue(Resource.Success(data.toList()))

            }else{
                dateList_.postValue(Resource.Error("NO DATA"))

            }
            }catch (e:Exception){
                dateList_.postValue(Resource.Error(e.localizedMessage.toString()))

            }
        }else{
            dateList_.postValue(Resource.Error("NO INTERNET CONNECTION"))

        }

    }


    fun markLocation(loactionModel: LocationModel):Call<Map<String, String>>{
        return retrofitApi.markLocation(loactionModel.User_Id,loactionModel.Admin_Id,loactionModel.Longitude.toString(),loactionModel.Latitude.toString(),loactionModel.Remark,loactionModel.Remark_)
    }

    private val locationData_ = MutableLiveData<Resource<List<LocationModel>>>()
    val locationData = locationData_

    suspend fun getLocationDateWise(user_id: String,date : String){


        if (NetworkUtils.isNetworkAvailable(context)){

            try {
                locationData_.postValue(Resource.Loading())

                val result =  retrofitApi.getLocationDateWise(user_id,date)
                if (result!!.body()!=null && result.body()!!.size > 0){

                    locationData_.postValue(Resource.Success(result.body()))

                }else{
                    locationData_.postValue(Resource.Error("NO DATA"))

                }
            }catch (e:Exception){
                locationData_.postValue(Resource.Error(e.localizedMessage.toString()))

            }
        }else{
            locationData_.postValue(Resource.Error("NO INTERNET CONNECTION"))

        }
    }


}