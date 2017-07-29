package utnfrsf.dondecurso.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Nivel(@SerializedName("id")
                 @Expose var id: Int,
                 @SerializedName("nombre")
                 @Expose var nombre: String) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun toString(): String {
        return nombre
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nombre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Nivel> {
        override fun createFromParcel(parcel: Parcel): Nivel {
            return Nivel(parcel)
        }

        override fun newArray(size: Int): Array<Nivel?> {
            return arrayOfNulls(size)
        }
    }

}