package com.example.demo.repository;

import com.example.demo.Reserva;
import com.example.demo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByUsuario(Usuario usuario);
    List<Reserva> findByUsuarioId(Long id); // ✅ añadido para usar solo el ID
}
