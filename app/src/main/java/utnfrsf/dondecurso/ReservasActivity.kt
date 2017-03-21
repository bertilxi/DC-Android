package utnfrsf.dondecurso

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import utnfrsf.dondecurso.adapter.ReservaAdapter
import utnfrsf.dondecurso.adapter.ReservaEspecialAdapter
import utnfrsf.dondecurso.domain.Reserva
import utnfrsf.dondecurso.domain.ReservaEspecial

class ReservasActivity : AppCompatActivity() {
    var reservas: ArrayList<Reserva> = ArrayList()
    var reservasEspeciales: ArrayList<ReservaEspecial> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)

        val recyclerView = findViewById(R.id.recyclerViewReservas) as RecyclerView
        val buttonReserva = findViewById(R.id.buttonNuevaConsulta) as Button

        buttonReserva.setOnClickListener { _ -> finish() }

        val mAdapter = ReservaAdapter(reservas)

        recyclerView.layoutManager = LinearLayoutManager(applicationContext) as RecyclerView.LayoutManager?
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mAdapter
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)

        reservas.addAll(intent.getParcelableArrayListExtra<Reserva>("reservas"))

        //Reservas Especiales
        val recyclerViewReservasEspeciales = findViewById(R.id.recyclerViewReservasEspeciales) as RecyclerView

        val mAdapterReservasEspeciales = ReservaEspecialAdapter(reservasEspeciales)

        recyclerViewReservasEspeciales.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewReservasEspeciales.itemAnimator = DefaultItemAnimator()
        recyclerViewReservasEspeciales.adapter = mAdapterReservasEspeciales
        recyclerViewReservasEspeciales.addItemDecoration(itemDecoration)

        reservasEspeciales.addAll(intent.getParcelableArrayListExtra<ReservaEspecial>("reservas_especiales"))
        Log.d("APP", reservasEspeciales.toString())
    }
}