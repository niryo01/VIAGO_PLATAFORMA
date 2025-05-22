package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class ReservaController {

    // Simulamos base de datos en memoria
    private List<Reserva> reservas = new ArrayList<>();

    // ✅ Muestra servicios.html como vista intermedia
    @GetMapping("/servicios")
    public String mostrarServicios(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        return "servicios"; // Vista intermedia con botón "Vehículo Privado"
    }

    // ✅ Cuando se hace clic en "Vehículo Privado"
    @GetMapping("/servicios/vehiculo-privado")
    public String showReservaForm(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        return "vehiculoPrivado"; // Formulario
    }

    @PostMapping("/servicios")
    public String processReserva(@RequestParam String origen,
                                 @RequestParam String fechaSalida,
                                 @RequestParam String horaInput,
                                 @RequestParam String destino,
                                 @RequestParam int numeroPersonas,
                                 HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Crear reserva con datos y generar ID
        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setIdReserva(UUID.randomUUID().toString());
        nuevaReserva.setIdUsuario(usuario.getIdUsuario());
        nuevaReserva.setIdConductor("conductor1"); // Lógica simulada
        nuevaReserva.setFechaReserva(fechaSalida);
        nuevaReserva.setHoraReserva(horaInput);
        nuevaReserva.setOrigenReserva(origen);
        nuevaReserva.setDestinoReserva(destino);

        double costoPorPersona = 10.0;
        double costoTotal = costoPorPersona * (numeroPersonas + 1); // +1 por el usuario
        nuevaReserva.setCostoReserva(costoTotal);

        reservas.add(nuevaReserva);
        session.setAttribute("misReservas", getReservasUsuario(usuario.getIdUsuario()));

        return "redirect:/viajes";
    }

    @GetMapping("/viajes")
    public String showViajes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Reserva> reservasUsuario = getReservasUsuario(usuario.getIdUsuario());
        model.addAttribute("reservas", reservasUsuario);
        return "viajes";
    }

    private List<Reserva> getReservasUsuario(String idUsuario) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getIdUsuario().equals(idUsuario)) {
                resultado.add(r);
            }
        }
        return resultado;
    }
}

