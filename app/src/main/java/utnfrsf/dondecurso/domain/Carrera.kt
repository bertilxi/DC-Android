package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Carrera(@SerializedName("id")
              @Expose var id: Int,
              @SerializedName("nombre")
              @Expose var nombre: String) : Serializable {

    fun withId(id: Int): Carrera {
        this.id = id
        return this
    }

    fun withNombre(nombre: String): Carrera {
        this.nombre = nombre
        return this
    }

    companion object {
        private const val serialVersionUID = -1836502662250495382L
    }

}