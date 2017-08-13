package utnfrsf.dondecurso.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import com.google.gson.internal.LinkedTreeMap
import io.paperdb.Paper
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.activity.ReservasActivity
import utnfrsf.dondecurso.common.*
import utnfrsf.dondecurso.domain.Carrera
import utnfrsf.dondecurso.domain.Comision
import utnfrsf.dondecurso.domain.Materia
import utnfrsf.dondecurso.domain.Nivel
import utnfrsf.dondecurso.service.Api
import utnfrsf.dondecurso.view.MyArrayAdapter
import utnfrsf.dondecurso.view.MySpinner
import java.text.SimpleDateFormat
import java.util.*

class QueryFragment : Fragment() {

    private var rootView: View? = null

    private var api = Api.service

    private var materias: ArrayList<Materia> = ArrayList()
    private var carreras: ArrayList<Carrera> = ArrayList()
    private var niveles: ArrayList<Nivel> = ArrayList()
    private var comisiones: ArrayList<Comision> = ArrayList()
    private var filteredMaterias: ArrayList<Materia> = ArrayList()

    private var carrera: Carrera? = null
    private var nivel: Nivel? = null
    private var materia: Materia? = null
    private var comision: Comision? = null

    private var adapterCarrera: MyArrayAdapter<Carrera>? = null
    private var adapterNivel: MyArrayAdapter<Nivel>? = null
    private var adapterMateria: MyArrayAdapter<Materia>? = null
    private var adapterComision: MyArrayAdapter<Comision>? = null

    private var textViewErrorCarrera: TextView? = null
    private var fab: FloatingActionButton? = null
    private var spinnerMateria: MySpinner? = null
    private var spinnerCarrera: Spinner? = null
    private var spinnerNivel: Spinner? = null
    private var spinnerComision: Spinner? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_query, container, false)

        initView()
        requestLoads()

        val myFormat = "dd MMMM yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        val myCalendar = Calendar.getInstance()

        textViewErrorCarrera = rootView!!.findView(R.id.textViewErrorCarrera)
        val textViewFecha = rootView!!.findView<TextView>(R.id.textViewFecha)

        textViewFecha.setOnClickListener({
            val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                textViewFecha.text = sdf.format(myCalendar.time)
            }
            DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        })

        textViewFecha.text = sdf.format(myCalendar.time)

        fab = rootView!!.findView<FloatingActionButton>(R.id.fab)

        fab!!.setOnClickListener({

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
                    activity.launchActivity(ReservasActivity())
                }
            }

        })

        return rootView
    }

    private fun requestLoads() {
        api.loadSubjects().enqueue({ _, response ->
            materias.clear()
            val mMaterias = response?.body() as LinkedTreeMap<String, Any>
            materias.addAll(Util.fromJson(mMaterias))
            processSubjectsLoad()
        }, { _, _ ->
            if (isAdded) {
                Snackbar.make(rootView!!, getString(utnfrsf.dondecurso.R.string.error_conexion), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(utnfrsf.dondecurso.R.string.reintentar), { requestLoads() })
                        .show()
            }
        })
    }

    private fun validar(): Boolean {
        val valido = carrera?.id != 0
        if (!valido) {
            mostrarErrorCarrera()
            fab?.isEnabled = false
        }
        return valido
    }

    private fun mostrarErrorCarrera() {
        textViewErrorCarrera!!.error = getString(utnfrsf.dondecurso.R.string.debe_seleccionar_una_carrera)
        textViewErrorCarrera!!.visibility = View.VISIBLE
        Snackbar.make(rootView!!, getString(utnfrsf.dondecurso.R.string.debe_seleccionar_una_carrera), Snackbar.LENGTH_LONG).show()
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

                spinnerMateria = rootView!!.findView(R.id.spinner_materia)
                spinnerCarrera = rootView!!.findView(R.id.spinnerCarrera)
                spinnerNivel = rootView!!.findView(R.id.spinner_nivel)
                spinnerComision = rootView!!.findView(R.id.spinner_comision)

                adapterCarrera = MyArrayAdapter(rootView!!.context, R.layout.spinner_item_dark, carreras, false)
                adapterNivel = MyArrayAdapter(rootView!!.context, R.layout.spinner_item_dark, niveles, true)
                adapterComision = MyArrayAdapter(rootView!!.context, R.layout.spinner_item_dark, comisiones, true)
                adapterMateria = MyArrayAdapter(rootView!!.context, R.layout.spinner_item_dark, filteredMaterias, true)

                spinnerMateria!!.adapter = adapterMateria
                spinnerCarrera!!.adapter = adapterCarrera
                spinnerNivel!!.adapter = adapterNivel
                spinnerComision!!.adapter = adapterComision

                spinnerCarrera!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        carrera = carreras[position]
                        processSubjectsLoad()
                        fab?.isEnabled = true
                        textViewErrorCarrera!!.visibility = View.GONE
                    }
                }

                spinnerMateria!!.setOnItemSelectedEvenIfUnchangedListener(object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        materia = filteredMaterias[position]
                        comisiones.clear()
                        comisiones.addAll(materia!!.comisiones!!)
                        if (comisiones.size != 1) {
                            comisiones.add(0, Comision(0, "Todas"))
                        }
                        adapterComision!!.notifyDataSetChanged()
                        spinnerComision!!.setSelection(adapterComision?.getPosition(comision)!!)
                    }
                })

                spinnerNivel!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        nivel = niveles[position]
                        processSubjectsLoad()
                    }
                }

                spinnerComision!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        comision = comisiones[position]
                    }
                }

                spinnerCarrera!!.setSelection(adapterCarrera?.getPosition(carrera)!!)
                spinnerNivel!!.setSelection(adapterNivel?.getPosition(nivel)!!)

                materias.add(materia!!)
                processSubjectsLoad()
                spinnerMateria!!.setSelection(adapterMateria?.getPosition(materia)!!)
            }
        }

    }
}
