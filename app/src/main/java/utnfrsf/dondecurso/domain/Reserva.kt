package utnfrsf.dondecurso.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Reserva: Parcelable{
    @SerializedName("nombre")
    @Expose
    var nombre: String? = null
    @SerializedName("comision")
    @Expose
    var comision: String? = null
    @SerializedName("aula")
    @Expose
    var aula: String? = null
    @SerializedName("horario")
    @Expose
    var horario: String? = null

    override fun toString(): String {
        return nombre!!
    }
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Reserva> = object : Parcelable.Creator<Reserva> {
            override fun createFromParcel(source: Parcel): Reserva{
                return Reserva(source)
            }

            override fun newArray(size: Int): Array<Reserva?> {
                return arrayOfNulls(size)
            }
        }
    }

    constructor(input: Parcel){
        nombre = input.readString()
        aula = input.readString()
        comision = input.readString()
        horario = input.readString()
    }

    constructor()

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeString(nombre)
        dest.writeString(aula)
        dest.writeString(comision)
        dest.writeString(horario)
    }

    override fun describeContents(): Int {
        return 0
    }
}