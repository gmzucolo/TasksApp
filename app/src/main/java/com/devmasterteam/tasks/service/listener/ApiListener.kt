package com.devmasterteam.tasks.service.listener

interface ApiListener<T> {
    fun onSuccess(result: T)
    fun onFailure(message: String)
}