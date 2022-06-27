package com.mihir.team360.mvvm.retrofit

import androidx.lifecycle.LiveData
import com.mihir.team360.mvvm.models.AdminModel
import com.mihir.team360.mvvm.models.LocationModel
import com.mihir.team360.mvvm.models.UserModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("admin.asmx/verifyLoginUser")
    fun verifyLoginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<List<UserModel>>

    @GET("admin.asmx/verifyLoginAdmin")
    fun verifyLoginAdmin(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<List<AdminModel>>


    @GET("admin.asmx/addEmployee")
    fun addEmployee(
        @Query("Admin_Id") Admin_Id: String,
        @Query("Admin_Name") Admin_Name: String,
        @Query("User_Name") User_Name: String,
        @Query("Phone") Phone: String,
        @Query("Email") Email: String,
        @Query("Username") Username: String,
        @Query("Password") Password: String,
        @Query("Remark") Remark: String,
        @Query("Remark_") Remark_: String,
    ): Call<Map<String, String>>

    @GET("admin.asmx/markLocation")
    fun markLocation(
        @Query("User_Id") User_Id: String,
        @Query("Admin_Id") Admin_Id: String,
        @Query("Longitude") Longitude: String,
        @Query("Latitude") Latitude: String,
        @Query("Remark") Remark: String,
        @Query("Remark_") Remark_: String
    ): Call<Map<String, String>>



    @GET("admin.asmx/getEmployee")
    suspend fun getEmployee(@Query("admin_id") admin_id: String): Response<List<UserModel>>


    @GET("admin.asmx/getLocationDate")
    suspend fun getLocationDate(@Query("user_id") user_id: String): Response<List<LocationModel>>

    @GET("admin.asmx/getLocationDateWise")
   suspend fun getLocationDateWise(
        @Query("user_id") user_id:   String,
        @Query("date_") date_: String
    ): Response<List<LocationModel>>


}