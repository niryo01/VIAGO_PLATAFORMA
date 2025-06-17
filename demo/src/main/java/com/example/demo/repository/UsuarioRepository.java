package com.example.demo.repository;

import com.example.demo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEntidadCorreo(String correo);
}