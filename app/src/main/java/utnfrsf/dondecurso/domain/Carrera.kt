package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Carrera(@SerializedName("id")
              @Expose var id: Int,
              @SerializedName("nombre")
              @Expose var nombre: String) : Serializable {

    private val serialVersionUID = -1836502662250495382L

    override fun toString(): String {
        return nombre
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Carrera

        if (id != other.id) return false
        if (nombre != other.nombre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + nombre.hashCode()
        return result
    }
}