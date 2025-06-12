// src/main/java/com/example/demo/dao/ReservaDAOImpl.java
package com.example.demo.dao;

import com.example.demo.Reserva;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ReservaDAOImpl implements ReservaDAO {

    private List<Reserva> reservas = new ArrayList<>();


    @Override
    public List<Reserva> obtenerTodas() {
        return reservas;
    }

    @Override
    public void agregar(Reserva reserva) {
        reservas.add(reserva);
    }

    @Override
    public void eliminar(String idReserva) {
        reservas.removeIf(r -> r.getIdReserva().equals(idReserva));
    }

    @Override
    public Reserva obtenerPorId(String idReserva) {
        return reservas.stream()
                .filter(r -> r.getIdReserva().equals(idReserva))
                .findFirst()
                .orElse(null);
    }

    @Override
public List<Reserva> obtenerReservasPorUsuario(String idUsuario) {
    List<Reserva> resultado = new ArrayList<>();
    for (Reserva r : reservas) {
        if (r.getIdUsuario().equals(idUsuario)) {
            resultado.add(r);
        }
    }
    return resultado;
}

@Override
public void asignarConductor(String idReserva, String idConductor) {
    for (Reserva r : reservas) {
        if (r.getIdReserva().equals(idReserva)) {
            r.setIdConductor(idConductor); // usamos el atributo existente
            break;
        }
    }
}

}
