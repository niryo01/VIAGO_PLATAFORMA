package com.example.demo.repository;

import com.example.demo.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEntidadCorreo(String correo);
}
