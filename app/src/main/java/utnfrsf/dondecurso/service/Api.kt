package utnfrsf.dondecurso.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object Api {

    private val BASE_URL = "http://www.frsf.utn.edu.ar/"

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()!!

    val service: ApiEndpoints = retrofit.create<ApiEndpoints>(ApiEndpoints::class.java)

}


