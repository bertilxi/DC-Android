package utnfrsf.dondecurso.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.paperdb.Paper
import utnfrsf.dondecurso.R
import utnfrsf.dondecurso.activity.FavoritoActivity
import utnfrsf.dondecurso.activity.ReservasActivity
import utnfrsf.dondecurso.common.async
import utnfrsf.dondecurso.common.findView
import utnfrsf.dondecurso.common.launchActivity
import utnfrsf.dondecurso.common.onUI
import utnfrsf.dondecurso.domain.Favorito
import java.text.SimpleDateFormat
import java.util.*

class FavouritesFragment : Fragment() {

    private var rootView: View? = null
    private var mRecyclerView: RecyclerView? = null
    private var fab: FloatingActionButton? = null

    private var favoritos: ArrayList<Favorito> = ArrayList<Favorito>()

    override fun onStart() {
        super.onStart()
        async {
            favoritos = Paper.book().read("favoritos", ArrayList<Favorito>())
            updateList(favoritos)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_favourites, container, false)

        mRecyclerView = rootView!!.findView<RecyclerView>(R.id.recyclerview_favoritos)
        mRecyclerView!!.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(activity)
        mRecyclerView!!.setLayoutManager(mLayoutManager)

        async {
            favoritos = Paper.book().read("favoritos", ArrayList<Favorito>())
            updateList(favoritos)
        }

        fab = rootView!!.findView(R.id.fab_nuevo_favorito)
        fab!!.setOnClickListener { activity.launchActivity(FavoritoActivity()) }

        return rootView
    }

    fun updateList(data: ArrayList<Favorito>) {
        onUI { mRecyclerView?.adapter = RecyclerAdapter(data) }
    }

    internal inner class RecyclerAdapter(var mFavoritos: ArrayList<Favorito>) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fila_favorito, parent, false)
            return ViewHolder(view)
        }

        fun update(data: ArrayList<Favorito>) {
            mFavoritos.clear()
            mFavoritos.addAll(data)
            onUI { notifyDataSetChanged() }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textViewMateria.setText(mFavoritos[position].materia.nombre)
            holder.textViewNivel.setText(mFavoritos[position].nivel.nombre)
            holder.textViewComision.setText(mFavoritos[position].comision.nombre)
            holder.fabEliminar.setOnClickListener {
                async {
                    favoritos.removeAt(position)
                    Paper.book().write("favoritos", favoritos)
                    updateList(favoritos)
                }
            }
            holder.fabBusqueda.setOnClickListener {
                val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)

                async {
                    Paper.book().write("fecha", fecha)
                    Paper.book().write("carrera", mFavoritos[position].carrera)
                    Paper.book().write("nivel", mFavoritos[position].nivel)
                    Paper.book().write("materia", mFavoritos[position].materia)
                    Paper.book().write("comision", mFavoritos[position].comision)
                    onUI {
                        activity.launchActivity(ReservasActivity())
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return mFavoritos.size
        }
    }

    internal inner class ViewHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val textViewMateria: TextView by lazy { row.findView<TextView>(R.id.textview_materia) }
        val textViewNivel: TextView by lazy { row.findView<TextView>(R.id.textview_nivel) }
        val textViewComision: TextView by lazy { row.findView<TextView>(R.id.textview_comision) }
        val fabEliminar: FloatingActionButton by lazy { row.findView<FloatingActionButton>(R.id.fab_eliminar) }
        val fabBusqueda: FloatingActionButton by lazy { row.findView<FloatingActionButton>(R.id.fab_busqueda) }
    }

}
