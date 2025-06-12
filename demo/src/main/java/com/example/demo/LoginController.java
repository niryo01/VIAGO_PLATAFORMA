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


// Google Guava
import com.google.common.base.Optional;
import com.google.common.base.Strings;

// Apache Commons Validator
import org.apache.commons.validator.routines.EmailValidator;

// SLF4J (usado por Logback)
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class); // Logger para monitorear eventos

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private ConductorDAO conductorDAO;

    @Autowired
    private AdminDAO adminDAO;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        // Apache Commons: validación de email
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) {
            model.addAttribute("error", "Correo inválido");
            logger.warn("Intento de login con correo inválido: {}", email);
            return "login";
        }

        Admin admin = adminDAO.obtenerAdmin();
        if (email.equals(admin.getEmail()) && password.equals(admin.getPassword())) {
            session.setAttribute("admin", email);
            logger.info("Inicio de sesión exitoso como ADMIN: {}", email);
            return "redirect:/admin";
        }
         // Apache Commons: validación de email 

        Conductor conductor = conductorDAO.obtenerPorCorreo(email);
        Optional<Conductor> optConductor = Optional.fromNullable(conductor); // Guava: Optional para evitar null

        if (optConductor.isPresent() &&
            !Strings.isNullOrEmpty(password) &&
            password.equals(optConductor.get().getContraseña())) {

            session.setAttribute("conductor", optConductor.get());
            logger.info("Inicio de sesión exitoso como CONDUCTOR: {}", email);
            return "redirect:/vistaConductor";
        }

        Usuario usuario = usuarioDAO.obtenerPorCorreo(email);
        Optional<Usuario> optUsuario = Optional.fromNullable(usuario); // Guava: Optional

        if (optUsuario.isPresent() &&
            !Strings.isNullOrEmpty(password) &&
            password.equals(optUsuario.get().getContraseña())) {

            session.setAttribute("usuario", optUsuario.get());
            logger.info("Inicio de sesión exitoso como USUARIO:: {}", email);
            return "redirect:/index";
        }

        // Si ningún login fue exitoso
        model.addAttribute("error", "Email o contraseña incorrectos");
        logger.warn("Fallo de inicio de sesión para: {}", email);
        return "login";
    }

    @GetMapping("/admin")
    public String showAdminPage(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            model.addAttribute("alerta", "Primero debes iniciar sesión");
            return "login";
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
