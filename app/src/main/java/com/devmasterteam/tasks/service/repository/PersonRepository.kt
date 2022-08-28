package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.repository.remote.PersonService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class PersonRepository(context: Context) : BaseRepository(context) {

    // Instancia o retrofit
    private val personRemoteService: PersonService =
        RetrofitClient.getervice(PersonService::class.java)

    fun login(email: String, password: String, listener: ApiListener<PersonModel>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = personRemoteService.login(email, password)
        executeCall(call, listener)
    }

    fun create(name: String, email: String, password: String, listener: ApiListener<PersonModel>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = personRemoteService.create(name, email, password)
        executeCall(call, listener)
    }
}