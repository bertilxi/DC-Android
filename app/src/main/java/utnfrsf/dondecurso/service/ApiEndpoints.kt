package utnfrsf.dondecurso.service

import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.http.*

interface ApiEndpoints {

    @GET("getMaterias.php")
    fun loadSubjects(): Call<LinkedTreeMap<String, Any>>

    @FormUrlEncoded
    @POST("getDistribucion.php")
    fun requestDistribution(@Field("fecha_inicio") fechaInicio: String,
                            @Field("carrera") carreraID: String,
                            @Field("nivel") nivelID: String,
                            @Field("materia") materiaID: String,
                            @Field("comisiones") comisionID: String): Call<String>

}
