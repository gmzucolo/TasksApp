package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {

        private lateinit var INSTANCE: Retrofit
        private var personKey: String = ""
        private var token: String = ""

        private fun getRetrofitInstance(): Retrofit {

            // Faz a conexão
            val httpClient = OkHttpClient.Builder()

            // Intercepta a conexão
            httpClient.addInterceptor(object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    // Pega a conexão e coloca parâmetros
                    val request = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, token)
                        .build()
                    // Retorna a requisição com os novos parâmetros
                    return chain.proceed(request)
                }

            })

            if (!::INSTANCE.isInitialized) {
                synchronized(RetrofitClient::class) {
                    INSTANCE = Retrofit.Builder()
                        .baseUrl("http://devmasterteam.com/CursoAndroidAPI/")
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        //metodo generico para chamar qualquer serviço
        fun <S> getervice(serviceClass: Class<S>): S {
            return getRetrofitInstance().create(serviceClass)
        }

        fun addHeaders(personKeyValue: String, tokenValue: String) {
            personKey = personKeyValue
            token = tokenValue
        }
    }
}