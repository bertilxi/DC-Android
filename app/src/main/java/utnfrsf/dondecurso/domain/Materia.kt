package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Materia : Serializable {

    private val serialVersionUID = -3380047840208817455L

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("id_carrera")
    @Expose
    var idCarrera: Int? = null
    @SerializedName("Comisiones")
    @Expose
    var Comisiones: List<Comision>? = null
    @SerializedName("nombre")
    @Expose
    var nombre: String? = null
    @SerializedName("nivel")
    @Expose
    var nivel: Int? = null

    override fun toString(): String {
        return nombre!!
    }

}