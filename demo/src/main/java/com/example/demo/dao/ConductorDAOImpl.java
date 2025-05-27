package com.example.demo.dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

import com.example.demo.Conductor;

@Repository
public class ConductorDAOImpl implements ConductorDAO {
    private List<Conductor> conductores = new ArrayList<>();

    public ConductorDAOImpl() {
        // Conductor de ejemplo
        conductores.add(new Conductor(
            "c1", "Carlos", "Lopez", "ABC123", true, "veh1",
            "conductor@viago.com", "conductor"
        ));
    }

    @Override
    public List<Conductor> obtenerTodos() {
        return conductores;
    }

    @Override
    public Conductor obtenerPorId(String idConductor) {
        return conductores.stream()
                .filter(c -> c.getIdConductor().equals(idConductor))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Conductor obtenerPorCorreo(String correo) {
        return conductores.stream()
                .filter(c -> c.getCorreo().equals(correo))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void agregar(Conductor conductor) {
        conductores.add(conductor);
    }

    @Override
    public void eliminar(String idConductor) {
        conductores.removeIf(c -> c.getIdConductor().equals(idConductor));
    }
    
}
