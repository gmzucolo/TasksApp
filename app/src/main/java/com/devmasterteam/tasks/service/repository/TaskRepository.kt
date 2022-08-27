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

    fun list(listener: ApiListener<List<TaskModel>>) {
        val call = taskRemoteService.list()
        listCall(call, listener)
    }

    fun listNext(listener: ApiListener<List<TaskModel>>) {
        val call = taskRemoteService.listNext()
        listCall(call, listener)
    }

    fun listOverdue(listener: ApiListener<List<TaskModel>>) {
        val call = taskRemoteService.listOverdue()
        listCall(call, listener)
    }

    fun create(taskModel: TaskModel, listener: ApiListener<Boolean>) {
        val call = taskRemoteService.create(
            taskModel.id,
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

    private fun listCall(call: Call<List<TaskModel>>, listener: ApiListener<List<TaskModel>>) {
        call.enqueue(object : Callback<List<TaskModel>> {
            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                handleSuccessResponse(response, listener)
            }

            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                handleFailureResponse(context, listener)
            }

        })

    }
}