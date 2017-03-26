package utnfrsf.dondecurso.service

import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import utnfrsf.dondecurso.common.isOnline
import java.io.File


class Api(context: Context) {

    private var httpCacheDirectory = File(context.cacheDir, "cache")

    private var cache: Cache = Cache(httpCacheDirectory, 10 * 1024 * 1024)

    private val REWRITE_CACHE_CONTROL_INTERCEPTOR = Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        val maxAge: Int = 60*5 // read from cache for 5 minutes
        val maxStale: Int = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
        if (isOnline(context)) {
            originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge, only-if-cached")
                    .build()
        } else {
            originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-stale=$maxStale, only-if-cached")
                    .build()
        }

    }

    private val client = OkHttpClient.Builder()
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .cache(cache)
            .build()!!

    private val BASE_URL = "http://www.frsf.utn.edu.ar/"

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()!!

    val service: ApiEndpoints = retrofit.create<ApiEndpoints>(ApiEndpoints::class.java)

}


