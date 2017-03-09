package utnfrsf.dondecurso.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import utnfrsf.dondecurso.domain.Carrera

class MyAdapter(context: Context?, resource: Int, objects: MutableList<Carrera>?) : ArrayAdapter<Carrera>(context, resource, objects) {

    var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var row = convertView;

        if (row == null) {
            row = inflater.inflate(android.R.layout.simple_spinner_item, parent, false)
        }

        // TODO: use de getTag() and setTag()

        var holder: ViewHolder = ViewHolder(row!!)

        holder.mTextView?.text = getItem(position).nombre

        return row
    }

    class ViewHolder(row: View) {
        var mTextView: TextView? = null

        init {
            mTextView = row.findViewById(android.R.id.text1) as TextView
        }
    }

}
