package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {
    private String idVehiculo;
    private String placa;
    private String modelo;
    private String marca;
    private int anio;
    private String color;
    private String idConductor;
}
