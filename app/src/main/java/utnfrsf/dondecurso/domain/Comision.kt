package utnfrsf.dondecurso.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Comision(@SerializedName("id")
                    @Expose
                    var id: Int? = null,
                    @SerializedName("nombre")
                    @Expose
                    var nombre: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as? Int,
            parcel.readString())

    override fun toString(): String {
        return nombre.toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(nombre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Comision> {
        override fun createFromParcel(parcel: Parcel): Comision {
            return Comision(parcel)
        }

        override fun newArray(size: Int): Array<Comision?> {
            return arrayOfNulls(size)
        }
    }

}