package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Nivel(@SerializedName("id")
            @Expose var id: Int,
            @SerializedName("nombre")
            @Expose var nombre: String) : Serializable {

    private val serialVersionUID = -2410057161709898979L

    override fun toString(): String {
        return nombre
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Nivel

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