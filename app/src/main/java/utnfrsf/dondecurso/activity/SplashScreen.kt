package utnfrsf.dondecurso.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.gson.internal.LinkedTreeMap
import io.paperdb.Paper
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.common.*
import utnfrsf.dondecurso.domain.Carrera
import utnfrsf.dondecurso.domain.Materia
import utnfrsf.dondecurso.domain.Nivel
import utnfrsf.dondecurso.service.Api
import java.util.*

class SplashScreen : AppCompatActivity() {

    private var carreras: ArrayList<Carrera> = ArrayList()
    private var niveles: ArrayList<Nivel> = ArrayList()
    private var materias: ArrayList<Materia> = ArrayList()
    private val api = Api.service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mantenimiento)
        val textviweUrl = findView<TextView>(R.id.textviewUrlWeb)

        textviweUrl.setOnClickListener { v ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urlWeb))))
        }

//        Paper.init(this)
//
//        async {
//            checkMetadata()
//            requestLoads()
//            val carrera = Paper.book().read<Carrera>("carrera")
//            onUI {
//                if (carrera == null || carrera.id <= 0) launchActivity(SetupActivity())
//                else launchActivity(MainActivity())
//                finish()
//            }
//        }
    }

    private fun requestLoads() {
        api.loadSubjects().enqueue({ _, response ->
            materias.clear()
            val mMaterias = response?.body() as LinkedTreeMap<String, Any>
            materias.addAll(Util.fromJson(mMaterias))
            Paper.book().write("materias", materias)
        }, { _, _ ->
//            Snackbar.make(this@SplashScreen, getString(utnfrsf.dondecurso.R.string.error_conexion), Snackbar.LENGTH_INDEFINITE)
//                    .setAction(getString(utnfrsf.dondecurso.R.string.reintentar), { requestLoads() })
//                    .show()
        })
    }

    fun checkMetadata(){

        carreras = Paper.book().read("carreras", ArrayList<Carrera>())
        niveles = Paper.book().read("niveles", ArrayList<Nivel>())

        if(!carreras.isEmpty() && !niveles.isEmpty()) return

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

        Paper.book().write("carreras", carreras)
        Paper.book().write("niveles", niveles)
    }

}