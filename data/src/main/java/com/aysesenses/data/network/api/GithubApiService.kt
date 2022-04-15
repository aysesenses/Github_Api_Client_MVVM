package com.aysesenses.data.network.api

import com.aysesenses.domain.model.UserDetail
import com.aysesenses.data.network.response.UserListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    //Annotate the method with @GET, specifying the endpoint for the JSON github users response,
    //and create the Retrofit REsponse object that will start the HTTP request
    @GET("search/users")
   suspend fun searchUser(@Query("q") q: String):
            Response<UserListResponse>

    @GET("users/{user}")
   suspend fun getUserDetails(@Path("user") user: String):
            Response<UserDetail>
}
