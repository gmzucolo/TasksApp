package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context) {

    private val taskRemoteService = RetrofitClient.getervice(TaskService::class.java)

    fun create(taskModel: TaskModel, listener: ApiListener<Boolean>) {
        val call = taskRemoteService.create(
            taskModel.priorityId,
            taskModel.description,
            taskModel.dueData,
            taskModel.complete
        )
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.code() == TaskConstants.HTTP.SUCCESS) {
                    // O objeto pego por meio do response.body é passado no método listener.onSuccess (objeto = it)
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onFailure(failResponse(response.errorBody()!!.string()))
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }

        })
    }

    private fun failResponse(string: String): String {
        return Gson().fromJson(string, String::class.java)
    }
}