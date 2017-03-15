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

}