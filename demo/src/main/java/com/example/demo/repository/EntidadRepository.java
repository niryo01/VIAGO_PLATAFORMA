package com.example.demo.repository;

import com.example.demo.Entidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntidadRepository extends JpaRepository<Entidad, Long> {
    Optional<Entidad> findByCorreo(String Correo);
}