package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/usuariosRegistrados")
    public String verUsuarios() {
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
