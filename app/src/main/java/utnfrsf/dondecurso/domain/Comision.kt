package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Comision() : Serializable {

    private val serialVersionUID = 5011134580201116098L

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("nombre")
    @Expose
    var nombre: String? = null

    constructor(id: Int?, nombre: String?) : this() {
        this.id = id
        this.nombre = nombre
    }

    override fun toString(): String {
        return nombre.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Comision

        if (id != other.id) return false
        if (nombre != other.nombre) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (nombre?.hashCode() ?: 0)
        return result
    }

}