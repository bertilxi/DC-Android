package utnfrsf.dondecurso.common

import com.google.gson.internal.LinkedTreeMap
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import utnfrsf.dondecurso.domain.Comision
import utnfrsf.dondecurso.domain.Materia
import utnfrsf.dondecurso.domain.Reserva
import utnfrsf.dondecurso.domain.ReservaEspecial

fun fromJson(objects: LinkedTreeMap<String, Any>): ArrayList<Materia> {
    val mMaterias: ArrayList<Materia> = ArrayList()

    for (key in objects.keys) {
        objects[key]
        val materiaNode: LinkedTreeMap<String, Any> = objects.getValue(key) as LinkedTreeMap<String, Any>

        val id: Double = materiaNode.getValue("id") as Double
        val idCarrera: Double = materiaNode.getValue("id_carrera") as Double
        val comisionesMap: LinkedTreeMap<String, Double> = materiaNode.getValue("comisiones") as LinkedTreeMap<String, Double>
        val nombre: Any = materiaNode.getValue("nombre")
        val nivel: Double = materiaNode.getValue("nivel") as Double

        val materia = Materia()
        materia.nombre = nombre.toString()
        materia.id = Math.round(id).toInt()
        materia.idCarrera = Math.round(idCarrera).toInt()
        materia.nivel = Math.round(nivel).toInt()
        materia.comisiones = ArrayList<Comision>()
        for (comKey in comisionesMap.keys) {
            val comisionNode: LinkedTreeMap<String, Any> = comisionesMap.getValue(comKey) as LinkedTreeMap<String, Any>
            val comID: Double = comisionNode.getValue("id") as Double
            val comNombre: Any = comisionNode.getValue("nombre")
            val comision: Comision = Comision()
            comision.id = Math.round(comID).toInt()
            comision.nombre = comNombre.toString()
            materia.comisiones!!.add(comision)
        }
        mMaterias.add(materia)
    }
    return mMaterias
}

fun fromJson(objects: String): ArrayList<Reserva> {
    val mReservas: ArrayList<Reserva> = ArrayList()

    val doc = Jsoup.parse(objects)
    if (doc.getElementsByClass("bloque").isEmpty()) {
        val tablas = doc.getElementsByTag("table")
        if (tablas.isNotEmpty()) {
            val tablaReservas = tablas[0]
            val filas = tablaReservas.getElementsByTag("tr")
            filas.removeAt(0)

            for (f in filas) {
                val columnas = f.getElementsByTag("td")
                val mReserva = Reserva()
                mReserva.comision = columnas[0].text()
                mReserva.horario = columnas[1].text()
                mReserva.nombre = columnas[2].text()
                mReserva.aula = columnas[3].text()
                mReservas.add(mReserva)
            }
        }
    }

    return mReservas
}

fun fromJsonReservasEspeciales(objects: String): ArrayList<ReservaEspecial> {
    val mReservas: ArrayList<ReservaEspecial> = ArrayList()

    val doc = Jsoup.parse(objects)
    val tablas = doc.getElementsByTag("table")
    if (tablas.isNotEmpty()) {
        var tablaReservasEspeciales: Element? = null
        if (doc.getElementsByClass("bloque").isEmpty()) {
            if (tablas.size >= 2) {
                tablaReservasEspeciales = tablas[1]
            }
        } else {
            tablaReservasEspeciales = tablas[0]
        }

        if (tablaReservasEspeciales != null) {
            val filas = tablaReservasEspeciales.getElementsByTag("tr")
            filas?.removeAt(0)

            for (f in filas!!) {
                val columnas = f.getElementsByTag("td")
                val mReserva = ReservaEspecial()
                val s = columnas[0].text()
                val split = s.split("Carrera: ", "Materia: ", "- ")
                if (split.size >= 2) {
                    mReserva.carrera = split[1]
                }
                if (split.size >= 3) {
                    mReserva.materia = split[2]
                }
                for(i in 4..split.size ){
                    mReserva.descripcion += split[i-1] + '\n'
                }

                mReserva.horario = columnas[1].text() + " a " + columnas[2].text()
                mReserva.aula = columnas[3].text()
                mReservas.add(mReserva)
            }
        }
    }

    return mReservas
}