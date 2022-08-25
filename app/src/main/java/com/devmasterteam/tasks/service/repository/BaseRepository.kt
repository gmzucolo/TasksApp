package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.google.gson.Gson
import retrofit2.Response

open class BaseRepository {
    // Pega o retorno Json e o converte para uma class String
    private fun failResponse(string: String): String {
        return Gson().fromJson(string, String::class.java)
    }

    fun <T> handleResponse(response: Response<T>, listener: ApiListener<T>) {
        if (response.code() == TaskConstants.HTTP.SUCCESS) {
            // O objeto pego por meio do response.body é passado no método listener.onSuccess (objeto = it)
            response.body()?.let { listener.onSuccess(it) }
        } else {
            listener.onFailure(failResponse(response.errorBody()!!.string()))
        }
    }
}