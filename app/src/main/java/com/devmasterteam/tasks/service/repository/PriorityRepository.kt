package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class PriorityRepository(val context: Context) {

    private val priorityRemoteService: PriorityService =
        RetrofitClient.getervice(PriorityService::class.java)

    fun list() {
        val call = priorityRemoteService.list()
    }
}
