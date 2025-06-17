package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {

    @Id
    private Long id;

    @OneToOne
    @MapsId // Este Admin comparte el mismo ID que Entidad
    @JoinColumn(name = "id") // FK y PK al mismo tiempo
    private Entidad entidad;

    private String nombre;
    private String apellido;
}
