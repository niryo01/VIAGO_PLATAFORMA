package com.example.demo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entidad")
    private Long id;

    @Column(name = "Correo", nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String Password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "entidad_rol", joinColumns = @JoinColumn(name = "Entidad_ID"), inverseJoinColumns = @JoinColumn(name = "Rol_ID"))
    private Set<Rol> roles = new HashSet<>();

    public Entidad(String correo, String password) {
        this.correo = correo;
        this.Password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Entidad))
            return false;
        Entidad entidad = (Entidad) o;
        return correo != null && correo.equals(entidad.correo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correo);
    }
}
