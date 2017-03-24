package utnfrsf.dondecurso

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.google.gson.internal.LinkedTreeMap
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import utnfrsf.dondecurso.adapter.MyArrayAdapter
import utnfrsf.dondecurso.common.fromJson
import utnfrsf.dondecurso.common.fromJsonReservasEspeciales
import utnfrsf.dondecurso.domain.*
import utnfrsf.dondecurso.service.Api
import utnfrsf.dondecurso.service.ApiEndpoints
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    var apiService: ApiEndpoints = Api().service
    var materias: ArrayList<Materia> = ArrayList()
    var carreras: ArrayList<Carrera> = ArrayList()
    var niveles: ArrayList<Nivel> = ArrayList()
    var reservas: ArrayList<Reserva> = ArrayList()
    var reservasEspeciales: ArrayList<ReservaEspecial> = ArrayList()

    var carrera: Carrera? = null
    var nivel: Nivel? = null
    var materia: Materia? = null
    var comision: Comision? = null

    var comisiones: ArrayList<Comision> = ArrayList()
    var filteredMaterias: ArrayList<Materia> = ArrayList()

    var adapterCarrera: MyArrayAdapter<Carrera>? = null
    var adapterNivel: MyArrayAdapter<Nivel>? = null
    var adapterMateria: MyArrayAdapter<Materia>? = null
    var adapterComision: MyArrayAdapter<Comision>? = null
    var preferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences = getPreferences(Context.MODE_PRIVATE)
        initData()

        adapterCarrera = MyArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, carreras, false)
        adapterNivel = MyArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, niveles, true)
        adapterComision = MyArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, comisiones, true)
        adapterMateria = MyArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, filteredMaterias, true)

        spinnerMateria.adapter = adapterMateria
        spinnerCarrera.adapter = adapterCarrera
        spinnerNivel.adapter = adapterNivel
        spinnerComision.adapter = adapterComision

        spinnerCarrera.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                carrera = carreras[position]
                processSubjectsLoad()
                buttonBuscar?.isEnabled = true
                textViewErrorCarrera.visibility = View.GONE
            }
        }

        spinnerMateria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                materia = filteredMaterias[position]
                comisiones.clear()
                comisiones.addAll(materia!!.comisiones!!)
                if (comisiones.size != 1) {
                    comisiones.add(0, Comision(0, "Todas"))
                }
                adapterComision!!.notifyDataSetChanged()
                spinnerComision.setSelection(adapterComision?.getPosition(comision)!!)
            }
        }

        spinnerNivel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                nivel = niveles[position]
                processSubjectsLoad()
            }
        }

        spinnerComision.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                comision = comisiones[position]
            }
        }

        spinnerCarrera.setSelection(adapterCarrera?.getPosition(carrera)!!)
        spinnerNivel.setSelection(adapterNivel?.getPosition(nivel)!!)
        materias.add(materia!!)
        processSubjectsLoad()
        spinnerMateria.setSelection(adapterMateria?.getPosition(materia)!!)

        apiService.loadSubjects().enqueue(object : Callback<LinkedTreeMap<String, Any>> {
            override fun onResponse(call: Call<LinkedTreeMap<String, Any>>?, response: Response<LinkedTreeMap<String, Any>>?) {
                val mMaterias = response?.body() as LinkedTreeMap<String, Any>
                materias.clear()
                materias.addAll(fromJson(mMaterias))
                processSubjectsLoad()
                spinnerMateria.setSelection(adapterMateria?.getPosition(materia)!!)
            }

            override fun onFailure(call: Call<LinkedTreeMap<String, Any>>?, t: Throwable?) {
                Log.d("APP", t.toString())
                Snackbar.make(constraintLayout, getString(R.string.error_conexion), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.reintentar), { apiService.loadSubjects().enqueue(this) })
                        .show()
            }
        })

        val myFormat = "dd MMMM yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        val myCalendar = Calendar.getInstance()

        textViewFecha.setOnClickListener({
            val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                textViewFecha.text = sdf.format(myCalendar.time)
            }
            DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        })
        textViewFecha.text = sdf.format(myCalendar.time)

        buttonBuscar?.setOnClickListener({
            if(validar()){
                apiService.requestDistribution(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.time),
                        carrera?.id.toString(),
                        nivel?.id.toString(),
                        materia?.id.toString(),
                        comision?.id.toString()).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>?, response: Response<String>?) {
                        val mReservas = response?.body() as String
                        reservas = fromJson(mReservas)
                        reservasEspeciales = fromJsonReservasEspeciales(mReservas)
                        val i = Intent(this@MainActivity, ReservasActivity::class.java)
                        i.putExtra("reservas", reservas)
                        i.putExtra("reservas_especiales", reservasEspeciales)
                        startActivity(i)
                    }

                    override fun onFailure(call: Call<String>?, t: Throwable?) {
                        Log.d("APP", t.toString())
                        Snackbar.make(constraintLayout, getString(R.string.error_conexion), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.reintentar), { buttonBuscar.callOnClick()})
                                .show()
                    }
                })
                val gson = Gson()
                preferences?.edit()!!
                        .putString("carrera", gson.toJson(carrera))
                        .putString("nivel", gson.toJson(nivel))
                        .putString("materia", gson.toJson(materia))
                        .putString("comision", gson.toJson(comision))
                        .apply()
            }
        })
    }

    private fun  validar(): Boolean {
        val valido = carrera?.id != 0
        if(!valido){
            mostrarErrorCarrera()
            buttonBuscar?.isEnabled = false
        }
        return valido
    }

    private fun mostrarErrorCarrera() {
        textViewErrorCarrera.error = getString(R.string.debe_seleccionar_una_carrera)
        textViewErrorCarrera.visibility = View.VISIBLE
        Snackbar.make(constraintLayout, getString(R.string.debe_seleccionar_una_carrera), Snackbar.LENGTH_LONG).show()
    }

    fun processSubjectsLoad() {
        filteredMaterias.clear()
        if (carrera?.id != 0 && nivel?.id != 0) {
            materias.asSequence()
                    .filter { it.idCarrera == carrera?.id && it.nivel == nivel?.id }
                    .forEach { filteredMaterias.add(it) }
        }
        else if(carrera?.id != 0 && nivel?.id == 0){
            materias.asSequence()
                    .filter { it.idCarrera == carrera?.id}
                    .forEach { filteredMaterias.add(it) }
        }
        if (filteredMaterias.size != 1) {
            filteredMaterias.add(0, Materia(0, "Todas"))
        }
        adapterMateria?.notifyDataSetChanged()
        spinnerMateria.setSelection(0)

        return
    }

    fun initData() {
        carreras.add(Carrera(0, "Seleccione una carrera"))
        carreras.add(Carrera(1, "Ingeniería en Sistemas"))
        carreras.add(Carrera(2, "Ingeniería Industrial"))
        carreras.add(Carrera(5, "Ingeniería Eléctrica"))
        carreras.add(Carrera(6, "Ingeniería Mecánica"))
        carreras.add(Carrera(7, "Ingeniería Civil"))
        carreras.add(Carrera(8, "Tecnicatura Superior en Mecatrónica"))
        carreras.add(Carrera(9, "Institucional"))
        carreras.add(Carrera(10, "Extensión Universitaria"))

        niveles.add(Nivel(0, "Todos"))
        niveles.add(Nivel(1, "Nivel 1"))
        niveles.add(Nivel(2, "Nivel 2"))
        niveles.add(Nivel(3, "Nivel 3"))
        niveles.add(Nivel(4, "Nivel 4"))
        niveles.add(Nivel(5, "Nivel 5"))
        niveles.add(Nivel(6, "Nivel 6"))
        niveles.add(Nivel(7, "No Corresponde"))

        val gson = Gson()
        carrera = gson.fromJson(preferences?.getString("carrera", "{'id':0, 'nombre':'Seleccione una carrera'}"), Carrera::class.java)
        nivel = gson.fromJson(preferences?.getString("nivel", "{'id':0, 'nombre':'Todos'}"), Nivel::class.java)
        materia = gson.fromJson(preferences?.getString("materia", "{'id':0, 'nombre':'Todas'}"), Materia::class.java)
        comision = gson.fromJson(preferences?.getString("comision", "{'id':0, 'nombre':'Todas'}"), Comision::class.java)
    }
}
