package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Nivel : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("nombre")
    @Expose
    var nombre: String? = null

    fun withId(id: Int?): Nivel {
        this.id = id
        return this
    }

    fun withNombre(nombre: String): Nivel {
        this.nombre = nombre
        return this
    }

    companion object {
        private const val serialVersionUID = -2410057161709898979L
    }

}