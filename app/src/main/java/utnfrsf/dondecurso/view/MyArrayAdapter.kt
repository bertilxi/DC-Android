package utnfrsf.dondecurso.view

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyArrayAdapter<T>(context: Context?, resource: Int, objects: List<T>?, var seleccionarPrimero: Boolean) : ArrayAdapter<T?>(context, resource, objects) {
    override fun isEnabled(position: Int): Boolean {
        return position != 0 || (position >= 0 && seleccionarPrimero)
    }

    override fun getDropDownView(position: Int, convertView: View?,
                                 parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val tv = view as TextView
        if (position == 0 && !seleccionarPrimero) {
            // Set the hint text color gray
            tv.setTextColor(Color.GRAY)
        } else {
            tv.setTextColor(Color.BLACK)
        }
        return view
    }
}
