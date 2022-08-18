package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.PersonModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PersonService {

    @POST("Authentication/Login")
    //body da requisição
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<PersonModel>

    @POST("Athentication/Create")
    @FormUrlEncoded
    fun create(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<PersonModel>
}