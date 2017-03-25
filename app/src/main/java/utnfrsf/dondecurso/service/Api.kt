package utnfrsf.dondecurso.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Api {
    val client = OkHttpClient.Builder().build()!!

    private val BASE_URL = "http://www.frsf.utn.edu.ar/"
    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()!!
    val service: ApiEndpoints = retrofit.create<ApiEndpoints>(ApiEndpoints::class.java)
}