package utnfrsf.dondecurso.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import com.google.gson.internal.LinkedTreeMap
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import utnfrsf.dondecurso.common.*
import utnfrsf.dondecurso.domain.Carrera
import utnfrsf.dondecurso.domain.Comision
import utnfrsf.dondecurso.domain.Materia
import utnfrsf.dondecurso.domain.Nivel
import utnfrsf.dondecurso.service.Api
import utnfrsf.dondecurso.view.MyArrayAdapter
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var api = Api.service

    var materias: ArrayList<Materia> = ArrayList()
    var carreras: ArrayList<Carrera> = ArrayList()
    var niveles: ArrayList<Nivel> = ArrayList()
    var comisiones: ArrayList<Comision> = ArrayList()
    var filteredMaterias: ArrayList<Materia> = ArrayList()

    var carrera: Carrera? = null
    var nivel: Nivel? = null
    var materia: Materia? = null
    var comision: Comision? = null

    var adapterCarrera: MyArrayAdapter<Carrera>? = null
    var adapterNivel: MyArrayAdapter<Nivel>? = null
    var adapterMateria: MyArrayAdapter<Materia>? = null
    var adapterComision: MyArrayAdapter<Comision>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(utnfrsf.dondecurso.R.layout.activity_main)

        initView()
        requestLoads()

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

            if (!validar()) {
                return@setOnClickListener
            }

            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(myCalendar.time)

            async {
                Paper.book().write("fecha", fecha)
                Paper.book().write("carrera", carrera)
                Paper.book().write("nivel", nivel)
                Paper.book().write("materia", materia)
                Paper.book().write("comision", comision)
                onUI {
                    launchActivity(ReservasActivity())
                }
            }

        })
    }

    private fun requestLoads() {
        api.loadSubjects().enqueue({ _, response ->
            materias.clear()
            val mMaterias = response?.body() as LinkedTreeMap<String, Any>
            materias.addAll(Util.fromJson(mMaterias))
            processSubjectsLoad()
        }, { _, _ ->
            Snackbar.make(constraintLayout, getString(utnfrsf.dondecurso.R.string.error_conexion), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(utnfrsf.dondecurso.R.string.reintentar), { requestLoads() })
                    .show()
        })
    }

    private fun validar(): Boolean {
        val valido = carrera?.id != 0
        if (!valido) {
            mostrarErrorCarrera()
            buttonBuscar?.isEnabled = false
        }
        return valido
    }

    private fun mostrarErrorCarrera() {
        textViewErrorCarrera.error = getString(utnfrsf.dondecurso.R.string.debe_seleccionar_una_carrera)
        textViewErrorCarrera.visibility = View.VISIBLE
        Snackbar.make(constraintLayout, getString(utnfrsf.dondecurso.R.string.debe_seleccionar_una_carrera), Snackbar.LENGTH_LONG).show()
    }

    fun processSubjectsLoad() {
        filteredMaterias.clear()
        if (carrera?.id != 0 && nivel?.id != 0) {
            materias.asSequence()
                    .filter { it.idCarrera == carrera?.id && it.nivel == nivel?.id }
                    .forEach { filteredMaterias.add(it) }
        } else if (carrera?.id != 0 && nivel?.id == 0) {
            materias.asSequence()
                    .filter { it.idCarrera == carrera?.id }
                    .forEach { filteredMaterias.add(it) }
        }
        if (filteredMaterias.size != 1) {
            filteredMaterias.add(0, Materia(0, "Todas"))
        }
        adapterMateria?.notifyDataSetChanged()

        if (filteredMaterias.contains(materia)) {
            spinnerMateria?.setSelection(adapterMateria?.getPosition(materia)!!)
        } else {
            spinnerMateria?.setSelection(0)
        }
    }

    fun initView() {

        async {

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

            carrera = Paper.book().read("carrera", Carrera(0, "Seleccione una carrera"))
            nivel = Paper.book().read("nivel", Nivel(0, "Todos"))
            materia = Paper.book().read("materia", Materia(0, "Todas"))
            comision = Paper.book().read("comision", Comision(0, "Todas"))

            onUI {

                adapterCarrera = MyArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, carreras, false)
                adapterNivel = MyArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, niveles, true)
                adapterComision = MyArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, comisiones, true)
                adapterMateria = MyArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, filteredMaterias, true)

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

                spinnerMateria.setOnItemSelectedEvenIfUnchangedListener(object : AdapterView.OnItemSelectedListener {
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
                })

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
            }
        }

    }
}

