package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.R
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.TaskModel
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService

class TaskRepository(context: Context) : BaseRepository(context) {

    private val taskRemoteService = RetrofitClient.getervice(TaskService::class.java)

    fun list(listener: ApiListener<List<TaskModel>>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = taskRemoteService.list()
        executeCall(call, listener)
    }

    fun listNext(listener: ApiListener<List<TaskModel>>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = taskRemoteService.listNext()
        executeCall(call, listener)
    }

    fun listOverdue(listener: ApiListener<List<TaskModel>>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = taskRemoteService.listOverdue()
        executeCall(call, listener)
    }

    fun create(taskModel: TaskModel, listener: ApiListener<Boolean>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = taskRemoteService.create(
            taskModel.id,
            taskModel.priorityId,
            taskModel.description,
            taskModel.dueDate,
            taskModel.complete
        )
        executeCall(call, listener)
    }

    fun update(taskModel: TaskModel, listener: ApiListener<Boolean>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = taskRemoteService.update(
            taskModel.id,
            taskModel.priorityId,
            taskModel.description,
            taskModel.dueDate,
            taskModel.complete
        )
        executeCall(call, listener)
    }

    fun delete(id: Int, listener: ApiListener<Boolean>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = taskRemoteService.delete(id)
        executeCall(call, listener)
    }

    fun complete(id: Int, listener: ApiListener<Boolean>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = taskRemoteService.complete(id)
        executeCall(call, listener)
    }

    fun undo(id: Int, listener: ApiListener<Boolean>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = taskRemoteService.undo(id)
        executeCall(call, listener)
    }

    fun load(id: Int, listener: ApiListener<TaskModel>) {

        if (!isConnectionAvailable()) {
            listener.onFailure(context.getString(R.string.ERROR_INTERNET_CONNECTION))
            return
        }

        val call = taskRemoteService.load(id)
        executeCall(call, listener)
    }
}