package utnfrsf.dondecurso

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utnfrsf.dondecurso.common.fromJson
import utnfrsf.dondecurso.domain.Carrera
import utnfrsf.dondecurso.domain.Comision
import utnfrsf.dondecurso.domain.Materia
import utnfrsf.dondecurso.domain.Nivel
import utnfrsf.dondecurso.service.Api
import utnfrsf.dondecurso.service.ApiEndpoints


class MainActivity : AppCompatActivity() {

    var apiService: ApiEndpoints = Api().service
    var materias: ArrayList<Materia> = ArrayList<Materia>()
    var carreras: ArrayList<Carrera> = ArrayList<Carrera>()
    var niveles: ArrayList<Nivel> = ArrayList<Nivel>()

    var carrera: Carrera? = null
    var nivel: Nivel? = null
    var materia: Materia? = null

    var comisiones: ArrayList<Comision> = ArrayList<Comision>()
    var filteredMaterias: ArrayList<Materia> = ArrayList<Materia>()

    var adapterMateria: ArrayAdapter<Materia>? = null
    var spinnerMateria: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerCarerra = findViewById(R.id.spinner_carrera) as Spinner
        val spinnerNivel = findViewById(R.id.spinner_nivel) as Spinner
        spinnerMateria = findViewById(R.id.spinner_materia) as Spinner

        initData()

        val adapterCarrera = ArrayAdapter<Carrera>(this, android.R.layout.simple_spinner_dropdown_item, carreras)
        val adapterNivel = ArrayAdapter<Nivel>(this, android.R.layout.simple_spinner_dropdown_item, niveles)

        spinnerCarerra.adapter = adapterCarrera
        spinnerNivel.adapter = adapterNivel

        spinnerCarerra.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                carrera = carreras.get(position)
                processSubjectsLoad()
            }
        }

        spinnerMateria!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                materia = materias[position]
            }
        }

        spinnerNivel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                nivel = niveles.get(position)
                processSubjectsLoad()
            }
        }

        apiService.loadSubjects().enqueue(object : Callback<LinkedTreeMap<String, Any>> {
            override fun onResponse(call: Call<LinkedTreeMap<String, Any>>?, response: Response<LinkedTreeMap<String, Any>>?) {
                val mMaterias = response?.body() as LinkedTreeMap<String, Any>
                materias = fromJson(mMaterias)
                processSubjectsLoad()
            }

            override fun onFailure(call: Call<LinkedTreeMap<String, Any>>?, t: Throwable?) {
                throw t!!
            }
        })
    }

    fun processSubjectsLoad() {
        if (carrera == null || nivel == null) {
            return
        }
        filteredMaterias = ArrayList()
        materias.asSequence()
                .filter { it.idCarrera == carrera?.id && it.nivel == nivel?.id }
                .forEach { filteredMaterias.add(it) }

        adapterMateria = ArrayAdapter<Materia>(this, android.R.layout.simple_spinner_dropdown_item, filteredMaterias)
        spinnerMateria?.adapter = adapterMateria
    }

    fun initData() {

        carreras.add(Carrera(1, "Ingeniería en Sistemas"))
        carreras.add(Carrera(2, "Ingeniería Industrial"))
        carreras.add(Carrera(5, "Ingeniería Eléctrica"))
        carreras.add(Carrera(6, "Ingeniería Mecánica"))
        carreras.add(Carrera(7, "Ingeniería Civil"))
        carreras.add(Carrera(8, "Tecnicatura Superior en Mecatrónica"))
        carreras.add(Carrera(9, "Institucional"))
        carreras.add(Carrera(10, "Extensión Universitaria"))

        niveles.add(Nivel(1, "Nivel 1"))
        niveles.add(Nivel(2, "Nivel 2"))
        niveles.add(Nivel(3, "Nivel 3"))
        niveles.add(Nivel(4, "Nivel 4"))
        niveles.add(Nivel(5, "Nivel 5"))
        niveles.add(Nivel(6, "Nivel 6"))
        niveles.add(Nivel(7, "No Corresponde"))

    }
}
