package utnfrsf.dondecurso.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReservaEspecial : Parcelable {
    @SerializedName("carrera")
    @Expose
    var carrera: String? = ""
    @SerializedName("materia")
    @Expose
    var materia: String? = ""
    @SerializedName("descripcion")
    @Expose
    var descripcion: String? = ""
    @SerializedName("aula")
    @Expose
    var aula: String? = ""
    @SerializedName("horario")
    @Expose
    var horario: String? = ""

    override fun toString(): String {
        return carrera!! + " " + materia!! + " " + descripcion!!
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ReservaEspecial> = object : Parcelable.Creator<ReservaEspecial> {
            override fun createFromParcel(source: Parcel): ReservaEspecial {
                return ReservaEspecial(source)
            }

            override fun newArray(size: Int): Array<ReservaEspecial?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(input: Parcel) {
        carrera = input.readString()
        materia = input.readString()
        descripcion = input.readString()
        aula = input.readString()
        horario = input.readString()
    }

    constructor()

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(carrera)
        dest.writeString(materia)
        dest.writeString(descripcion)
        dest.writeString(aula)
        dest.writeString(horario)
    }

    override fun describeContents(): Int {
        return 0
    }
}