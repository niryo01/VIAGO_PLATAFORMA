package com.example.demo.repository;

import com.example.demo.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConductorRepository extends JpaRepository<Conductor, Long> {
    Conductor findByEntidadCorreo(String correo);
}