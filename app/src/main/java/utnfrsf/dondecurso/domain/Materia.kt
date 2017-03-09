package utnfrsf.dondecurso.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Materia : Serializable {

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

    fun withId(id: Int?): Materia {
        this.id = id
        return this
    }

    fun withIdCarrera(idCarrera: Int?): Materia {
        this.idCarrera = idCarrera
        return this
    }

    fun withComisions(Comisions: List<Comision>): Materia {
        this.Comisiones = Comisions
        return this
    }

    fun withNombre(nombre: String): Materia {
        this.nombre = nombre
        return this
    }

    fun withNivel(nivel: Int): Materia {
        this.nivel = nivel
        return this
    }

    companion object {
        private const val serialVersionUID = -3380047840208817455L
    }

}