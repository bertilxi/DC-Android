package utnfrsf.dondecurso.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import io.paperdb.Paper
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.common.async
import utnfrsf.dondecurso.common.findView
import utnfrsf.dondecurso.common.onUI
import utnfrsf.dondecurso.domain.*
import utnfrsf.dondecurso.view.MyArrayAdapter
import utnfrsf.dondecurso.view.MySpinner

class FavoritoActivity : AppCompatActivity() {

    private val spinnerNivel: Spinner by lazy { findView<Spinner>(R.id.spinner_nivel) }
    private val spinnerMateria: MySpinner by lazy { findView<MySpinner>(R.id.spinner_materia) }
    private val spinnerComision: Spinner by lazy { findView<Spinner>(R.id.spinner_comision) }
    private val fab: FloatingActionButton by lazy { findView<FloatingActionButton>(R.id.fab_favorito) }

    private var adapterNivel: MyArrayAdapter<Nivel>? = null
    private var adapterMateria: MyArrayAdapter<Materia>? = null
    private var adapterComision: MyArrayAdapter<Comision>? = null

    private var niveles: ArrayList<Nivel> = ArrayList<Nivel>()
    private var materias: ArrayList<Materia> = ArrayList<Materia>()
    private var filteredMaterias: ArrayList<Materia> = ArrayList<Materia>()
    private var comisiones: ArrayList<Comision> = ArrayList<Comision>()
    private var favoritos: ArrayList<Favorito> = ArrayList<Favorito>()

    private var carrera: Carrera? = null
    private var nivel: Nivel? = null
    private var materia: Materia? = null
    private var comision: Comision? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorito)

        val toolbar = findView<Toolbar>(R.id.toolbar_favorito)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        async {
            carrera = Paper.book().read("carrera")
            niveles = Paper.book().read("niveles")
            materias = Paper.book().read("materias", ArrayList())
            comisiones = Paper.book().read("comisiones", ArrayList())
            favoritos = Paper.book().read("favoritos", ArrayList())

            onUI { setupView() }
        }
    }

    fun setupView() {
        adapterNivel = MyArrayAdapter(this@FavoritoActivity, R.layout.spinner_item_dark, niveles, true)
        adapterComision = MyArrayAdapter(this@FavoritoActivity, R.layout.spinner_item_dark, comisiones, true)
        adapterMateria = MyArrayAdapter(this@FavoritoActivity, R.layout.spinner_item_dark, filteredMaterias, true)

        spinnerMateria.adapter = adapterMateria
        spinnerNivel.adapter = adapterNivel
        spinnerComision.adapter = adapterComision

        spinnerMateria.setOnItemSelectedEvenIfUnchangedListener(object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                materia = filteredMaterias[position]
                comisiones.clear()
                comisiones.addAll(materia!!.comisiones!!)
                if (comisiones.size != 1) {
                    comisiones.add(0, Comision(0, "Todas"))
                }
                if (comisiones.size == 1) {
                   comision = comisiones[0]
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

        fab.setOnClickListener {

            if (nivel == null) return@setOnClickListener
            if (materia == null) return@setOnClickListener
            if (comision == null) return@setOnClickListener

            async {
                val favorito = Favorito(carrera!!, nivel!!, materia!!, comision!!)
                favoritos.add(favorito)
                Paper.book().write("favoritos", favoritos)
                onUI { finish() }
            }

        }
    }

    fun processSubjectsLoad() {
        filteredMaterias.clear()
        if (carrera!!.id != 0 && nivel?.id != 0) {
            materias.asSequence()
                    .filter { it.idCarrera == carrera!!.id && it.nivel == nivel?.id }
                    .forEach { filteredMaterias.add(it) }
        } else if (carrera!!.id != 0 && nivel?.id == 0) {
            materias.asSequence()
                    .filter { it.idCarrera == carrera!!.id }
                    .forEach { filteredMaterias.add(it) }
        }
        if (filteredMaterias.size != 1) {
            filteredMaterias.add(0, Materia(0, "Todas"))
        }
        adapterMateria?.notifyDataSetChanged()

        if (filteredMaterias.contains(materia)) {
            spinnerMateria.setSelection(adapterMateria?.getPosition(materia)!!)
        } else {
            spinnerMateria.setSelection(0)
        }
    }
}
