package utnfrsf.dondecurso.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Materia(@SerializedName("id")
                   @Expose
                   var id: Int? = null,
                   @SerializedName("nombre")
                   @Expose
                   var nombre: String? = null,
                   @SerializedName("id_carrera")
                   @Expose
                   var idCarrera: Int? = null,
                   @SerializedName("comisiones")
                   @Expose
                   var comisiones: ArrayList<Comision>? = ArrayList(),
                   @SerializedName("nivel")
                   @Expose
                   var nivel: Int? = null) : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readArrayList(Comision::class.java.classLoader) as ArrayList<Comision>,
            parcel.readInt())

    override fun toString(): String {
        return nombre!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nombre)
        parcel.writeValue(idCarrera)
        parcel.writeValue(nivel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Materia> {
        override fun createFromParcel(parcel: Parcel): Materia {
            return Materia(parcel)
        }

        override fun newArray(size: Int): Array<Materia?> {
            return arrayOfNulls(size)
        }
    }
}