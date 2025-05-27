package com.example.demo;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.AdminDAO;
import com.example.demo.dao.ConductorDAO;
import com.example.demo.dao.UsuarioDAO;


@Controller
public class LoginController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private ConductorDAO conductorDAO;

    @Autowired
    private AdminDAO adminDAO;

    // Ya no necesitas constructor, Spring inyecta automáticamente

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        Admin admin = adminDAO.obtenerAdmin();
        if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
            session.setAttribute("admin", email);
            return "redirect:/admin";
        }

        Conductor conductor = conductorDAO.obtenerPorCorreo(email);
        if (conductor != null && password.equals(conductor.getContraseña())) {
            session.setAttribute("conductor", conductor);
            return "redirect:/vistaConductor";
        }

        Usuario usuario = usuarioDAO.obtenerPorCorreo(email);
        if (usuario != null && password.equals(usuario.getContraseña())) {
            session.setAttribute("usuario", usuario);
            return "redirect:/index";
        }

        model.addAttribute("error", "Email o contraseña incorrectos");
        return "login";
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
