package utnfrsf.dondecurso.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class Api {
    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)!!
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()!!

    private val BASE_URL = "http://www.frsf.utn.edu.ar/"
    val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()!!
    val service: ApiEndpoints = retrofit.create<ApiEndpoints>(ApiEndpoints::class.java)
}