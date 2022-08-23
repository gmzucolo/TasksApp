package com.devmasterteam.tasks.service.repository

import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import com.devmasterteam.tasks.service.repository.remote.TaskService

class TaskRepository {

    private val taskRemoteService = RetrofitClient.getervice(TaskService::class.java)
}