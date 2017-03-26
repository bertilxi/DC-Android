package utnfrsf.dondecurso.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.domain.ReservaEspecial
import java.util.*

class ReservaEspecialAdapter(var reservas: ArrayList<ReservaEspecial>) : Adapter<ReservaEspecialAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val reserva: ReservaEspecial = reservas[position]
        holder!!.materia.text = reserva.materia
        holder.descripcion.text = reserva.descripcion
        holder.horario.text = reserva.horario
        holder.aula.text = reserva.aula
    }

    override fun getItemCount(): Int {
        return reservas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.fila_reserva_especial, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var materia: TextView = itemView?.findViewById(R.id.textViewMateriaEspecial) as TextView
        var descripcion: TextView = itemView?.findViewById(R.id.textViewDescripcionEspecial) as TextView
        var horario: TextView = itemView?.findViewById(R.id.textViewHorarioEspecial) as TextView
        var aula: TextView = itemView?.findViewById(R.id.textViewAulaEspecial) as TextView
    }
}