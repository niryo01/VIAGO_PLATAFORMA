package com.example.demo;

import com.example.demo.repository.ConductorRepository;
import com.example.demo.repository.ReservaRepository;
import com.example.demo.repository.UsuarioRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    @Autowired
    private final ReservaRepository reservaRepository;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final ConductorRepository conductorRepository;

    @GetMapping("/servicios")
    public String mostrarServicios(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            logger.warn("Acceso no autorizado a /servicios");
            return "redirect:/login";
        }
        logger.info("Usuario accedió a la vista de servicios.");
        return "servicios";
    }

    @GetMapping("/servicios/vehiculo-privado")
    public String showReservaForm(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            logger.warn("Acceso no autorizado a /servicios/vehiculo-privado");
            return "redirect:/login";
        }
        logger.info("Usuario accedió al formulario de reserva de vehículo privado.");
        return "vehiculoPrivado";
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
            logger.warn("Intento de reservar sin sesión activa.");
            return "redirect:/login";
        }

        usuario = usuarioRepository.findById(usuario.getId()).orElse(null);
        if (usuario == null) {
            logger.error("Usuario en sesión no encontrado en base de datos.");
            return "redirect:/login";
        }

            if (origen == null || origen.trim().isEmpty() || destino == null || destino.trim().isEmpty()) {
        logger.warn("Origen o destino vacío. No se puede procesar la reserva.");
        session.setAttribute("errorReserva", "Debe completar el origen y destino.");
        return "redirect:/servicios/vehiculo-privado"; 
    }

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setFechaReserva(fechaSalida);
        nuevaReserva.setHoraReserva(horaInput);
        nuevaReserva.setCostoReserva(10.0 * numeroPersonas);
        nuevaReserva.setCantidadReservada(numeroPersonas);
        nuevaReserva.setOrigenReserva(origen);
        nuevaReserva.setDestinoReserva(destino);

        reservaRepository.save(nuevaReserva);
        logger.info("Reserva creada exitosamente para usuario ID: {}", usuario.getId());

        List<Reserva> reservasUsuario = reservaRepository.findByUsuario(usuario);
        session.setAttribute("misReservas", reservasUsuario);

        return "redirect:/viajes";
    }

    @GetMapping("/viajes")
    public String showViajes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            logger.warn("Intento de acceso a /viajes sin sesión.");
            return "redirect:/login";
        }

        List<Reserva> reservasUsuario = reservaRepository.findByUsuario(usuario);
        model.addAttribute("reservas", reservasUsuario);
        logger.info("Se muestran las reservas del usuario ID: {}", usuario.getId());
        return "viajes";
    }

    @PostMapping("/admin/reserva/asignarConductor")
    public String asignarConductor(@RequestParam Long idReserva,
                                   @RequestParam Long idConductor) {
        logger.info("Asignación de conductor {} a reserva {}", idConductor, idReserva);
        Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);
        Optional<Conductor> conductorOpt = conductorRepository.findById(idConductor);

        if (reservaOpt.isPresent() && conductorOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            Conductor conductor = conductorOpt.get();
            reserva.setConductor(conductor);
            reservaRepository.save(reserva);
            logger.info("Conductor asignado correctamente.");
        } else {
            logger.warn("No se pudo asignar conductor. Reserva o conductor no encontrados.");
        }

        return "redirect:/admin/reserva/detalle/" + idReserva;
    }

    @GetMapping("/reservas/{id}/word")
    public void descargarReservaWord(@PathVariable Long id, HttpServletResponse response) throws IOException {
        logger.info("Generación de archivo Word para la reserva ID: {}", id);
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isEmpty()) {
            logger.warn("Reserva con ID {} no encontrada", id);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Reserva reserva = reservaOpt.get();

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=Reserva_" + reserva.getId() + ".docx");

        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Detalles de la Reserva");
            titleRun.setBold(true);
            titleRun.setFontSize(20);
            titleRun.addBreak();

            XWPFTable table = document.createTable(8, 2);
            table.getRow(0).getCell(0).setText("ID Reserva");
            table.getRow(0).getCell(1).setText(String.valueOf(reserva.getId()));

            table.getRow(1).getCell(0).setText("Correo Usuario");
            table.getRow(1).getCell(1).setText(reserva.getUsuario().getEntidad().getCorreo());

            table.getRow(2).getCell(0).setText("Correo Conductor");
            String correoConductor = reserva.getConductor() != null
                    ? reserva.getConductor().getUsuario().getEntidad().getCorreo()
                    : "No asignado";
            table.getRow(2).getCell(1).setText(correoConductor);

            table.getRow(3).getCell(0).setText("Fecha Reserva");
            table.getRow(3).getCell(1).setText(reserva.getFechaReserva());

            table.getRow(4).getCell(0).setText("Hora Reserva");
            table.getRow(4).getCell(1).setText(reserva.getHoraReserva());

            table.getRow(5).getCell(0).setText("Origen");
            table.getRow(5).getCell(1).setText(reserva.getOrigenReserva() != null ? reserva.getOrigenReserva() : "N/A");

            table.getRow(6).getCell(0).setText("Destino");
            table.getRow(6).getCell(1).setText(reserva.getDestinoReserva() != null ? reserva.getDestinoReserva() : "N/A");

            table.getRow(7).getCell(0).setText("Costo");
            table.getRow(7).getCell(1).setText(String.format("%.2f", reserva.getCostoReserva()));

            ServletOutputStream out = response.getOutputStream();
            document.write(out);
            out.flush();
        }
    }
}
