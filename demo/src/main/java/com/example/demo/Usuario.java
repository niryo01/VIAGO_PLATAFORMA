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
public class Usuario {

    @Id
    @Column(name = "id_usuario") 
    private Long id;

    @OneToOne
    @MapsId // Comparte la misma PK con la Entidad
    @JoinColumn(name = "id_usuario") // Clave foránea que es también la PK
    private Entidad entidad;

    private String nombre;
    private String apellido;
    private String telefono;
}
