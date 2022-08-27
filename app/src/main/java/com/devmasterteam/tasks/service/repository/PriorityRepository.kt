package com.devmasterteam.tasks.service.repository

import android.content.Context
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.repository.local.TaskDatabase
import com.devmasterteam.tasks.service.repository.remote.PriorityService
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) : BaseRepository() {

    private val priorityRemoteService: PriorityService =
        RetrofitClient.getervice(PriorityService::class.java)

    private val priorityDatabase = TaskDatabase.getDatabase(context).priorityDAO()

    companion object {
        private val cache = mutableMapOf<Int, String>()
        fun getDescription(id: Int): String {
            return cache[id] ?: ""
        }

        fun setDescription(id: Int, string: String) {
            cache[id] = string
        }
    }

    fun getDescription(id: Int): String {
        val cached = PriorityRepository.getDescription(id)
        return if (cached == "") {
            val description = priorityDatabase.getDescription(id)
            PriorityRepository.setDescription(id, description)
            description
        } else {
            cached
        }
    }

    fun list(): List<PriorityModel> = priorityDatabase.list()

    fun list(listener: ApiListener<List<PriorityModel>>) {
        val call = priorityRemoteService.list()
        call.enqueue(object : Callback<List<PriorityModel>> {
            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                handleSuccessResponse(response, listener)
            }

            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                handleFailureResponse(context, listener)
            }

        })
    }

    fun save(list: List<PriorityModel>) {
        priorityDatabase.clear()
        priorityDatabase.save(list)
    }
}
