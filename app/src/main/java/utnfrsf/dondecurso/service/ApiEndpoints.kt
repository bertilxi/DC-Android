package utnfrsf.dondecurso.service

import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiEndpoints {

    @GET("getMaterias.php")
    fun loadSubjects(): Call<LinkedTreeMap<String, Any>>

    @POST("getDistribucion.php")
    fun requestDistribution(@Query("fecha_inicio") fechaInicio: String,
                            @Query("carrera") carreraID: String,
                            @Query("nivel") nivelID: String,
                            @Query("materia") materiaID: String,
                            @Query("comisiones") comisionID: String): Call<String>

}
