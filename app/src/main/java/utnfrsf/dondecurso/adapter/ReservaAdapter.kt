package utnfrsf.dondecurso.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.domain.Reserva
import java.util.ArrayList
import android.view.LayoutInflater



class ReservaAdapter(var reservas: ArrayList<Reserva>) : Adapter<ReservaAdapter.MyViewHolder>(){
    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        val reserva : Reserva = reservas[position]
        holder!!.materia!!.text = reserva.nombre
        holder.horario!!.text = reserva.horario
        holder.comision!!.text = reserva.comision
        holder.aula!!.text = reserva.aula
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
        var materia : TextView? = null
        var horario : TextView? = null
        var aula : TextView? = null
        var comision : TextView? = null

        init {
            materia = itemView!!.findViewById(R.id.textViewMateria) as TextView?
            horario = itemView.findViewById(R.id.textViewHorario) as TextView?
            aula = itemView.findViewById(R.id.textViewAula) as TextView?
            comision = itemView.findViewById(R.id.textViewComision) as TextView?
        }
    }
}