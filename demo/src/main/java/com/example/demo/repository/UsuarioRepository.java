package com.example.demo.repository;

import com.example.demo.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEntidadCorreo(String correo);

    @Query("SELECT u FROM Usuario u JOIN u.entidad.roles r WHERE r.nombre = 'USUARIO'")
    List<Usuario> findUsuariosConRolUsuario();
}