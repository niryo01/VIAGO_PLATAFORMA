package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private String idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String contraseña;
    private String telefono;
}
