package utnfrsf.dondecurso.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {
    val service: ApiEndpoints

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create<ApiEndpoints>(ApiEndpoints::class.java)
    }

    companion object {
        private val BASE_URL = "http://172.10.1.143:8080/"
    }

}
