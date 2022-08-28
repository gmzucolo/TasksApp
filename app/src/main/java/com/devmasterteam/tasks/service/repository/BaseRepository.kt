package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseRepository(val context: Context) {
    // Pega o retorno Json e o converte para uma class String
    private fun failResponse(string: String): String {
        return Gson().fromJson(string, String::class.java)
    }

    fun <T> handleSuccessResponse(response: Response<T>, listener: ApiListener<T>) {
        if (response.code() == TaskConstants.HTTP.SUCCESS) {
            // O objeto pego por meio do response.body é passado no método listener.onSuccess (objeto = it)
            response.body()?.let { listener.onSuccess(it) }
        } else {
            listener.onFailure(failResponse(response.errorBody()!!.string()))
        }
    }

    fun <T> handleFailureResponse(context: Context, listener: ApiListener<T>) {
        listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
    }

    fun <T> executeCall(call: Call<T>, listener: ApiListener<T>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                handleSuccessResponse(response, listener)
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                handleFailureResponse(context, listener)
            }

        })
    }
}