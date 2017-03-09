package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Comision : Serializable {

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("nombre")
    @Expose
    var nombre: String? = null

    fun withId(id: Int?): Comision {
        this.id = id
        return this
    }

    fun withNombre(nombre: String): Comision {
        this.nombre = nombre
        return this
    }

    companion object {
        private const val serialVersionUID = 5011134580201116098L
    }

}