package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Reserva: Serializable{
    @SerializedName("id_carrera")
    @Expose
    var id_carrera: Int? = 0
    @SerializedName("nombre")
    @Expose
    var nombre: String? = null
    @SerializedName("comision")
    @Expose
    var comision: String? = null
    @SerializedName("aula")
    @Expose
    var aula: String? = null
    @SerializedName("horario")
    @Expose
    var horario: String? = null

    //private val serialVersionUID = -1836502662250495382L

    override fun toString(): String {
        return nombre!!
    }
}