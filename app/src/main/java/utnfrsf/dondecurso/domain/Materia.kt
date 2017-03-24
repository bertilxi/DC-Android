package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Materia() : Serializable {

    private val serialVersionUID = -3380047840208817455L

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("id_carrera")
    @Expose
    var idCarrera: Int? = null
    @SerializedName("comisiones")
    @Expose
    var comisiones: ArrayList<Comision>? = ArrayList()
    @SerializedName("nombre")
    @Expose
    var nombre: String? = null
    @SerializedName("nivel")
    @Expose
    var nivel: Int? = null

    constructor(id: Int?, nombre: String?) : this() {
        this.id = id
        this.nombre = nombre
    }

    override fun toString(): String {
        return nombre!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Materia

        if (id != other.id) return false
        if (idCarrera != other.idCarrera) return false
        if (comisiones != other.comisiones) return false
        if (nombre != other.nombre) return false
        if (nivel != other.nivel) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (idCarrera ?: 0)
        result = 31 * result + (comisiones?.hashCode() ?: 0)
        result = 31 * result + (nombre?.hashCode() ?: 0)
        result = 31 * result + (nivel ?: 0)
        return result
    }

}