package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    private String idReserva;
    private String idUsuario;
    private String idConductor;
    private String fechaReserva;
    private String horaReserva;
    private String origenReserva;
    private String destinoReserva;
    private double costoReserva;
}
