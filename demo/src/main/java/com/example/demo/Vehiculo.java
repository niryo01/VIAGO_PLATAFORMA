package com.example.demo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {

    @Id
    @Column(name = "id_vehiculo")
    private long id;


    @Column(name = "año")
    private int anio;

    private String color;
    private String marca;
    private String modelo;
    private String placa;

}
