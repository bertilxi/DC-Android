package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Nivel(@SerializedName("id")
                 @Expose var id: Int,
                 @SerializedName("nombre")
                 @Expose var nombre: String) {

    override fun toString(): String {
        return nombre
    }

}