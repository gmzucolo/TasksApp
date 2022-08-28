package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(context: Context) : BaseRepository(context) {

    // Instancia o retrofit
    private val personRemoteService: PersonService =
        RetrofitClient.getervice(PersonService::class.java)

    fun login(email: String, password: String, listener: ApiListener<PersonModel>) {
        val call = personRemoteService.login(email, password)
        executeCall(call, listener)
    }
}