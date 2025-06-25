package com.example.demo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Conductor conductor;

    @Column(name = "cantidad_reservada")
    private int cantidadReservada;

    @Column(name = "costo_reserva")
    private double costoReserva;

    @Column(name = "fecha_reserva")
    private String fechaReserva;

    @Column(name = "hora_reserva")
    private String horaReserva;

    @Column(name = "origen_reserva")
    private String origenReserva;

    @Column(name = "destino_reserva")
    private String destinoReserva;
}
