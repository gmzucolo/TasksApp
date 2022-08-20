package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.constants.TaskConstants
import com.devmasterteam.tasks.service.listener.ApiListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.PriorityModel
import com.devmasterteam.tasks.service.model.ValidationModel
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.PriorityRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository: PersonRepository =
        PersonRepository(application.applicationContext)

    private val priorityRepository: PriorityRepository =
        PriorityRepository(application.applicationContext)

    private val securityPreferences: SecurityPreferences =
        SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object : ApiListener<PersonModel> {
            override fun onSuccess(result: PersonModel) {
                // Salva os dados que vêm do header (autenticação) no SharedPreference
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name)
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token)

                // Adiciona os dados salvos do SharedPreference nos headers
                RetrofitClient.addHeaders(result.personKey, result.token)

                _login.value = ValidationModel()
            }

            override fun onFailure(message: String) {
                _login.value = ValidationModel(message)
            }


        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val person = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val token = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)

        RetrofitClient.addHeaders(person, token)

        val logged = (person != "" && token != "")
        _loggedUser.value = logged

        if (!logged) {
            priorityRepository.list(object : ApiListener<List<PriorityModel>> {
                override fun onSuccess(result: List<PriorityModel>) {
                    priorityRepository.save(result)
                }

                override fun onFailure(message: String) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

}