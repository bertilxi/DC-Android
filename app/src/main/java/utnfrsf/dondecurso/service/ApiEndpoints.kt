package utnfrsf.dondecurso.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import utnfrsf.dondecurso.domain.Materia

interface ApiEndpoints {

    @GET("materias")
    fun loadSubjects(): Call<List<Materia>>

    @POST("getDistribucion.php")
    fun requestDistribution(@Query("fecha_inicio") fechaInicio: String,
                            @Query("carrera") carreraID: String,
                            @Query("nivel") nivelID: String,
                            @Query("materia") materiaID: String,
                            @Query("comisiones") comisionID: String): Call<String>

}
