package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Comision : Serializable {

    private val serialVersionUID = 5011134580201116098L

    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("nombre")
    @Expose
    var nombre: String? = null

    override fun toString(): String {
        return nombre.toString()
    }

}