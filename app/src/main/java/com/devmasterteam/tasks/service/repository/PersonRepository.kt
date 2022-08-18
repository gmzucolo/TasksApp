package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository {

    private val personRemoteService: PersonService = RetrofitClient.getervice(PersonService::class.java)

    fun login(email: String, password: String) {
        val call = personRemoteService.login(email, password)
        call.enqueue(object : Callback<PersonModel> {
            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                TODO("Not yet implemented")
            }

            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}