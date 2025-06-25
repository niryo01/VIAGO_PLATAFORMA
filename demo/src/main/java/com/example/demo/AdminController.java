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

import org.springframework.ui.Model;

@Controller
public class AdminController {

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/usuariosRegistrados")
    public String verUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findUsuariosConRolUsuario());
        return "usuariosRegistrados";
    }

    @GetMapping("/conductores")
    public String verConductores(Model model) {
        model.addAttribute("conductores", conductorRepository.findAll());
        return "conductoresRegistrados";
    }

    @GetMapping("/admin/reserva/detalle/{id}")
    public String verDetalleReserva(@PathVariable Long id, Model model) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id); // ✅ USO CORRECTO
        if (reservaOpt.isEmpty()) {
            return "error"; // o vista personalizada de error
        }

        List<Conductor> conductores = conductorRepository.findAll(); // ✅ USO CORRECTO

        model.addAttribute("reserva", reservaOpt.get());
        model.addAttribute("conductores", conductores);
        return "detallesReserva";
    }

    @GetMapping("/solicitudes")
    public String verSolicitudes() {
        return "reservasRegistradas";
    }

    @GetMapping("/admin/reservas/{id}")
    public String verReservasDelUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id); // ✅ USO CORRECTO
        if (usuarioOpt.isEmpty()) {
            return "error";
        }

        List<Reserva> reservas = reservaRepository.findByUsuarioId(id); // ✅ REQUIERE QUE ESTE MÉTODO EXISTA

        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("reservas", reservas);

        return "reservasRegistradas";
    }
}
