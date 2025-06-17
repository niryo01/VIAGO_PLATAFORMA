package com.example.demo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    private String idVehiculo;

    private String placa;
    private String modelo;
    private String marca;
    private int anio;
    private String color;

    @OneToOne
    @JoinColumn(name = "conductor_id")
    private Conductor conductor;
}
