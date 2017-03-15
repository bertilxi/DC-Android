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
}