package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.repository.ConductorRepository;
import com.example.demo.repository.ReservaRepository;
import com.example.demo.repository.UsuarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

@Controller
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/usuariosRegistrados")
    public String verUsuarios(Model model) {
        logger.info("Vista de usuarios registrados solicitada por el administrador.");
        model.addAttribute("usuarios", usuarioRepository.findUsuariosConRolUsuario());
        return "usuariosRegistrados";
    }

    @GetMapping("/conductores")
    public String verConductores(Model model) {
        logger.info("Vista de conductores registrados solicitada por el administrador.");
        model.addAttribute("conductores", conductorRepository.findAll());
        return "conductoresRegistrados";
    }

    @GetMapping("/admin/reserva/detalle/{id}")
    public String verDetalleReserva(@PathVariable Long id, Model model) {
        logger.info("Detalle de reserva solicitado para ID: {}", id);
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isEmpty()) {
            logger.warn("No se encontró reserva con ID: {}", id);
            return "error";
        }

        List<Conductor> conductores = conductorRepository.findAll();
        model.addAttribute("reserva", reservaOpt.get());
        model.addAttribute("conductores", conductores);
        return "detallesReserva";
    }

    @GetMapping("/solicitudes")
    public String verSolicitudes() {
        logger.info("Vista de solicitudes de reserva solicitada.");
        return "reservasRegistradas";
    }

    @GetMapping("/admin/reservas/{id}")
    public String verReservasDelUsuario(@PathVariable Long id, Model model) {
        logger.info("Vista de reservas solicitada para usuario ID: {}", id);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            logger.warn("No se encontró usuario con ID: {}", id);
            return "error";
        }

        List<Reserva> reservas = reservaRepository.findByUsuarioId(id);
        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("reservas", reservas);

        return "reservasRegistradas";
    }
}
