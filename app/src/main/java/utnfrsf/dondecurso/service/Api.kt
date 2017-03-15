package utnfrsf.dondecurso.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {
    // private val BASE_URL = "http://172.10.1.143:8080/"
    private val BASE_URL = "http://www.frsf.utn.edu.ar/"
    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()!!
    val service: ApiEndpoints = retrofit.create<ApiEndpoints>(ApiEndpoints::class.java)
}
