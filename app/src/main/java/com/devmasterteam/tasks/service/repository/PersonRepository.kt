package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository {

    // Instancia o retrofit
    private val personRemoteService: PersonService =
        RetrofitClient.getervice(PersonService::class.java)

    fun login(email: String, password: String, listener: ApiListener<PersonModel>) {
        val call = personRemoteService.login(email, password)
        call.enqueue(object : Callback<PersonModel> {
            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                if (response.code() == 200) {
                    // O objeto pego por meio do response.body é passado no método listener.onSuccess (objeto = it)
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onFailure(response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                listener.onFailure("")
            }

        })
    }
}