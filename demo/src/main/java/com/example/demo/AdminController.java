package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.dao.ConductorDAO;
import com.example.demo.dao.ReservaDAO;
import com.example.demo.dao.UsuarioDAO;

import org.springframework.ui.Model;

@Controller
public class AdminController {

    @Autowired
private ConductorDAO conductorDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private ReservaDAO reservaDAO;

    @GetMapping("/usuariosRegistrados")
    public String verUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioDAO.obtenerTodos());
        return "usuariosRegistrados";
    }

    @GetMapping("/conductores")
public String verConductores(Model model) {
    model.addAttribute("conductores", conductorDAO.obtenerTodos());
    return "conductoresRegistrados";
}

@GetMapping("/admin/reserva/detalle/{id}")
public String verDetalleReserva(@PathVariable String id, Model model) {
    Reserva reserva = reservaDAO.obtenerPorId(id); // usa tu lógica real
    List<Conductor> conductores = conductorDAO.obtenerTodos(); // o de la memoria

    model.addAttribute("reserva", reserva);
    model.addAttribute("conductores", conductores);
    return "detallesReserva";
}



    @GetMapping("/solicitudes")
    public String verSolicitudes() {
        return "reservasRegistradas";
    }

    @GetMapping("/admin/reservas/{id}")
public String verReservasDelUsuario(@PathVariable String id, Model model) {
    Usuario usuario = usuarioDAO.obtenerPorId(id);
    List<Reserva> reservas = reservaDAO.obtenerReservasPorUsuario(id);

    model.addAttribute("usuario", usuario);
    model.addAttribute("reservas", reservas);

    return "reservasRegistradas";
}
}
