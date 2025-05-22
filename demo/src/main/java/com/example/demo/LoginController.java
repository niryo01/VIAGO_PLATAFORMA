package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final Admin admin = new Admin("admin@viago.com", "admin123");
    private final ConductorLogin conductor = new ConductorLogin("conductor@viago.com", "1234");

    // Usuario de ejemplo
    private final Usuario usuario = new Usuario("user1", "Juan", "Perez", "usuario@viago.com", "user123", "999999999");

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
            session.setAttribute("admin", email);
            return "redirect:/admin";
        } else if (email.equals(conductor.getEmail()) && password.equals(conductor.getPassword())) {
            session.setAttribute("conductor", email);
            return "redirect:/vistaConductor";
        } else if (email.equals(usuario.getCorreo()) && password.equals(usuario.getContraseña())) {
            session.setAttribute("usuario", usuario);
            return "redirect:/index"; // redirige al formulario de reserva
        } else {
            model.addAttribute("error", "Email o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/admin")
    public String showAdminPage(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            model.addAttribute("alerta", "Primero debes iniciar sesión");
            return "login"; // retornamos la vista login con alerta
        }
        return "vistaAdmin";
    }

    @GetMapping("/vistaConductor")
    public String showConductorPage(HttpSession session, Model model) {
        if (session.getAttribute("conductor") == null) {
            model.addAttribute("alerta", "Primero debes iniciar sesión");
            return "login";
        }
        return "vistaConductor";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
