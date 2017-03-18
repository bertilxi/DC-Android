package utnfrsf.dondecurso

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import utnfrsf.dondecurso.adapter.ReservaAdapter
import utnfrsf.dondecurso.domain.Reserva


class ReservasActivity : AppCompatActivity() {
    var reservas : ArrayList<Reserva> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)

        var recyclerView = findViewById(R.id.recyclerViewReservas) as RecyclerView

        var mAdapter = ReservaAdapter(reservas)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = mAdapter

        reservas.addAll(intent.getParcelableArrayListExtra<Reserva>("reservas"))
    }
}
