package com.example.demo.dao;

import com.example.demo.Admin;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDAOImpl implements AdminDAO {
    private final Admin admin;

    public AdminDAOImpl() {
        // usuario definido
        this.admin = new Admin("admin@viago.com", "admin123");
    }

    @Override
    public Admin obtenerAdmin() {
        return admin;
    }
    
}
