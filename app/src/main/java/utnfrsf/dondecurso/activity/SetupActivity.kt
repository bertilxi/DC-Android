package utnfrsf.dondecurso.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import io.paperdb.Paper
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.common.async
import utnfrsf.dondecurso.common.findView
import utnfrsf.dondecurso.common.launchActivity
import utnfrsf.dondecurso.common.onUI
import utnfrsf.dondecurso.domain.Carrera
import utnfrsf.dondecurso.view.MyArrayAdapter

class SetupActivity : AppCompatActivity() {

    private val spinner: Spinner by lazy { findView<Spinner>(R.id.spinner_carrera) }
    private val textViewError: TextView by lazy { findView<TextView>(R.id.textview_error) }
    private val fab: FloatingActionButton by lazy { findView<FloatingActionButton>(R.id.fab_setup) }
    private var carreras: ArrayList<Carrera> = ArrayList<Carrera>()
    private var carrera: Carrera? = null
    private var adapter: MyArrayAdapter<Carrera>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        async {
            carreras = Paper.book().read("carreras", ArrayList<Carrera>())
            onUI { setupView() }
        }

    }

    fun setupView() {
        adapter = MyArrayAdapter(this@SetupActivity, R.layout.spinner_item, carreras, false)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                carrera = null
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                carrera = carreras[position]
                if (carrera!!.id == 0) carrera = null
            }
        }

        fab.setOnClickListener {
            if (carrera == null) {
                textViewError.visibility = View.VISIBLE
                textViewError.text = ""
                textViewError.error = "Seleccione una carrera por favor"
                return@setOnClickListener
            }
            async {
                Paper.book().write("carrera", carrera)
                onUI {
                    launchActivity(MainActivity())
                    finish()
                }
            }
        }
    }

}
