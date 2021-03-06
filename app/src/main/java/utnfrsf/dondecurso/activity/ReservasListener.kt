package utnfrsf.dondecurso.activity

import utnfrsf.dondecurso.domain.Reserva
import utnfrsf.dondecurso.domain.ReservaEspecial

interface ReservasListener {
    fun obtenerReservas(): ArrayList<Reserva>
    fun obtenerReservasEspeciales(): ArrayList<ReservaEspecial>
}