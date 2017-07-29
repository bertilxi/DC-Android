package utnfrsf.dondecurso.domain

import android.os.Parcel
import android.os.Parcelable

data class Favorito(val carrera: Carrera, val nivel: Nivel, val materia: Materia, val comision: Comision) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(Carrera::class.java.classLoader),
            parcel.readParcelable(Nivel::class.java.classLoader),
            parcel.readParcelable(Materia::class.java.classLoader),
            parcel.readParcelable(Comision::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(carrera, flags)
        parcel.writeParcelable(nivel, flags)
        parcel.writeParcelable(materia, flags)
        parcel.writeParcelable(comision, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Favorito> {
        override fun createFromParcel(parcel: Parcel): Favorito {
            return Favorito(parcel)
        }

        override fun newArray(size: Int): Array<Favorito?> {
            return arrayOfNulls(size)
        }
    }
}
