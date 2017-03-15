package utnfrsf.dondecurso.common

import android.util.Log
import com.google.gson.internal.LinkedTreeMap
import utnfrsf.dondecurso.domain.Comision
import utnfrsf.dondecurso.domain.Materia

fun fromJson(objects: LinkedTreeMap<String, Any>): ArrayList<Materia> {
    val mMaterias: ArrayList<Materia> = ArrayList()

    Log.v("keys", objects.keys.toString())

    for (key in objects.keys) {
        objects.get(key)
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
        val comisiones = ArrayList<Comision>()
        for (comKey in comisionesMap.keys) {
            if (!objects.contains(comKey)) {
                break
            }
            val comisionNode: LinkedTreeMap<String, Any> = objects.getValue(comKey) as LinkedTreeMap<String, Any>
            val comID: Double = comisionNode.getValue("id") as Double
            val comNombre: Any = comisionNode.getValue("nombre")
            val comision: Comision = Comision()
            comision.id = Math.round(comID).toInt()
            comision.nombre = comNombre.toString()
            comisiones.add(comision)
        }
        mMaterias.add(materia)
    }
    return mMaterias
}
