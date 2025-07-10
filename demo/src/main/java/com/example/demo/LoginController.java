package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.repository.UsuarioRepository;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        // Validación basica del correo con Apache Commons Validator
        EmailValidator validator = EmailValidator.getInstance();
        if (!validator.isValid(email)) {
            model.addAttribute("error", "Correo inválido");
            logger.warn("Intento de login con correo inválido: {}", email);
            return "login";
        }

        Usuario usuario = usuarioRepository.findByEntidadCorreo(email);

        // Verificación con BCrypt para prevenir autenticación rota
        if (usuario != null && passwordEncoder.matches(password, usuario.getEntidad().getPassword())) {

            if (usuario.getEntidad().getRoles().stream()
                    .anyMatch(rol -> rol.getNombre().equalsIgnoreCase("usuario"))) {

                session.setAttribute("usuario", usuario);
                logger.info("Inicio de sesión exitoso como USUARIO: {}", email);
                return "redirect:/index";

            } else if (usuario.getEntidad().getRoles().stream()
                    .anyMatch(rol -> rol.getNombre().equalsIgnoreCase("admin"))) {

                session.setAttribute("admin", email);
                logger.info("Inicio de sesión exitoso como ADMIN: {}", email);
                return "redirect:/admin";

            } else if (usuario.getEntidad().getRoles().stream()
                    .anyMatch(rol -> rol.getNombre().equalsIgnoreCase("conductor"))) {

                session.setAttribute("conductor", email);
                logger.info("Inicio de sesión exitoso como CONDUCTOR: {}", email);
                return "redirect:/vistaConductor";
            }
        }

        // Si el usuario no existe o la contraseña no coincide
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
