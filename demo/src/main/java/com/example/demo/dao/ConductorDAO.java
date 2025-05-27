package com.example.demo.dao;

import java.util.List;

import com.example.demo.Conductor;

public interface ConductorDAO {
    List<Conductor> obtenerTodos();
    Conductor obtenerPorId(String idConductor);
    Conductor obtenerPorCorreo(String correo);
    void agregar(Conductor conductor);
    void eliminar(String idConductor);

}
