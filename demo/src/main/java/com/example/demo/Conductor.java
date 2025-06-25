package com.example.demo;

import jakarta.persistence.Column;
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
public class Conductor {

    @Id
    @Column(name = "id_conductor")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_conductor")
    private Usuario usuario;

    @Column(name = "Licencia_Conducir")
    private String licenciaConducir;

    @Column(name = "Disponibilidad")
    private boolean disponibilidad;

    @OneToOne
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;
}
