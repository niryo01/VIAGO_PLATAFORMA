package com.example.demo;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.dao.UsuarioDAO;
import com.example.demo.dao.UsuarioDAOImpl;

import org.springframework.ui.Model;

@Controller
public class AdminController {

     private UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    @GetMapping("/usuariosRegistrados")
    public String verUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioDAO.obtenerTodos());
        return "usuariosRegistrados";
    }

    @GetMapping("/conductores")
    public String verConductores() {
        return "conductoresRegistrados";
    }

    @GetMapping("/solicitudes")
    public String verSolicitudes() {
        return "reservasRegistradas";
    }
    
}
