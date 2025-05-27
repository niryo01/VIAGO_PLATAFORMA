package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conductor {
    private String idConductor;
    private String nombre;
    private String apellido;
    private String licenciaConducir;
    private boolean disponibilidad;
    private String idVehiculo;
    private String correo;
    private String contraseña;
}
