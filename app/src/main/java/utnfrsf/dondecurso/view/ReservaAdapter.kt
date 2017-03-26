package utnfrsf.dondecurso.view

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.domain.Reserva
import java.util.*


class ReservaAdapter(var reservas: ArrayList<Reserva>) : Adapter<ReservaAdapter.MyViewHolder>() {
    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val reserva: Reserva = reservas[position]
        holder!!.materia.text = reserva.nombre
        holder.horario.text = reserva.horario
        holder.comision.text = reserva.comision
        holder.aula.text = reserva.aula
    }

    override fun getItemCount(): Int {
        return reservas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.fila_reserva, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var materia: TextView = itemView?.findViewById(R.id.textViewMateria) as TextView
        var horario: TextView = itemView?.findViewById(R.id.textViewHorario) as TextView
        var aula: TextView = itemView?.findViewById(R.id.textViewAula) as TextView
        var comision: TextView = itemView?.findViewById(R.id.textViewComision) as TextView
    }
}