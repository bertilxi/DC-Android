package utnfrsf.dondecurso.view

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.common.findView
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

    class MyViewHolder(view: View?) : RecyclerView.ViewHolder(view) {
        var materia: TextView = view?.findView<TextView>(R.id.textViewMateriaEspecial)!!
        var descripcion: TextView = view?.findView<TextView>(R.id.textViewDescripcionEspecial)!!
        var horario: TextView = view?.findView<TextView>(R.id.textViewHorarioEspecial)!!
        var aula: TextView = view?.findView<TextView>(R.id.textViewAulaEspecial)!!
    }
}