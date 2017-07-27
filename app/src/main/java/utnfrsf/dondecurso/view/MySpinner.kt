package utnfrsf.dondecurso.view

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner

class MySpinner(context: Context?, attrs: AttributeSet?) : Spinner(context, attrs) {
    var listener: OnItemSelectedListener? = null

    override fun setSelection(position: Int) {
        super.setSelection(position)
        listener?.onItemSelected(null, null, position, 0)
    }

    fun setOnItemSelectedEvenIfUnchangedListener(listener: OnItemSelectedListener) {
        this.listener = listener
    }
}