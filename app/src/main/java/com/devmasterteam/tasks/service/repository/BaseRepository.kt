package com.devmasterteam.tasks.service.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseRepository(val context: Context) {
    // Pega o retorno Json e o converte para uma class String
    private fun failResponse(string: String): String {
        try {
            val string = Gson().fromJson(string, String::class.java)
        } catch (e: JsonSyntaxException) {
            Toast.makeText(context, "Preencha corretamente os dados", Toast.LENGTH_SHORT).show()
        }
        return string
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

    fun isConnectionAvailable(): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNet = cm.activeNetwork ?: return false
            val networkCapabilities = cm.getNetworkCapabilities(activeNet) ?: return false
            result = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            if (cm.activeNetworkInfo != null) {
                result = when (cm.activeNetworkInfo!!.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return result
    }
}