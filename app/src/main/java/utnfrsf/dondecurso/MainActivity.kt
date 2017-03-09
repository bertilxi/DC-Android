package utnfrsf.dondecurso

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utnfrsf.dondecurso.domain.Carrera
import utnfrsf.dondecurso.domain.Comision
import utnfrsf.dondecurso.domain.Materia
import utnfrsf.dondecurso.domain.Nivel
import utnfrsf.dondecurso.service.Api
import utnfrsf.dondecurso.service.ApiEndpoints

class MainActivity : AppCompatActivity() {

    var apiService: ApiEndpoints? = null
    var materias: ArrayList<Materia> = ArrayList<Materia>()
    var carrera: Carrera? = null
    var nivel: Nivel? = null
    var comisiones: ArrayList<Comision> = ArrayList<Comision>()
    var filteredMaterias: ArrayList<Materia> = ArrayList<Materia>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = Api().service

        apiService!!.loadSubjects().enqueue(object : Callback<List<Materia>> {
            override fun onResponse(call: Call<List<Materia>>?, response: Response<List<Materia>>?) {
                materias = response?.body() as ArrayList<Materia>
            }

            override fun onFailure(call: Call<List<Materia>>?, t: Throwable?) {
                throw t!!
            }

        })

    }

    fun processSubjectsLoad() {
        filteredMaterias = materias
        filteredMaterias.filter {
            it.idCarrera == carrera?.id
            it.nivel == nivel?.id
        }
    }

}
