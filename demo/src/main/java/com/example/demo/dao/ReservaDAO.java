package com.example.demo.dao;

import com.example.demo.Reserva;
import java.util.List;

public interface ReservaDAO {
    List<Reserva> obtenerTodas();
    void agregar(Reserva reserva);
    void eliminar(String idReserva);
    Reserva obtenerPorId(String idReserva);
    List<Reserva> obtenerReservasPorUsuario(String idUsuario);
    void asignarConductor(String idReserva, String idConductor);

}
