package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context: Context) : BaseRepository() {

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
                handleSuccessResponse(response, listener)
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                handleFailureResponse(context, listener)
            }

        })
    }
}