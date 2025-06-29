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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReservaController {

    @Autowired
    private final ReservaRepository reservaRepository;

    @Autowired
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private final ConductorRepository conductorRepository;

    // Mostrar página de servicios (requiere sesión)
    @GetMapping("/servicios")
    public String mostrarServicios(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        return "servicios";
    }

    // Mostrar formulario para "Vehículo Privado"
    @GetMapping("/servicios/vehiculo-privado")
    public String showReservaForm(HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        return "vehiculoPrivado";
    }

    // Procesar reserva (crear)
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

        usuario = usuarioRepository.findById(usuario.getId()).orElse(null);
        if (usuario == null) {
            return "redirect:/login";
        }

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setFechaReserva(fechaSalida);
        nuevaReserva.setHoraReserva(horaInput);
        nuevaReserva.setCostoReserva(10.0 * (numeroPersonas ));
        nuevaReserva.setCantidadReservada(numeroPersonas );
        nuevaReserva.setOrigenReserva(origen);
        nuevaReserva.setDestinoReserva(destino);

        reservaRepository.save(nuevaReserva);

        List<Reserva> reservasUsuario = reservaRepository.findByUsuario(usuario);
        session.setAttribute("misReservas", reservasUsuario);

        return "redirect:/viajes";
    }

    @GetMapping("/viajes")
    public String showViajes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Reserva> reservasUsuario = reservaRepository.findByUsuario(usuario);
        model.addAttribute("reservas", reservasUsuario);
        return "viajes";
    }

    @PostMapping("/admin/reserva/asignarConductor")
    public String asignarConductor(@RequestParam Long idReserva,
            @RequestParam Long idConductor) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);
        Optional<Conductor> conductorOpt = conductorRepository.findById(idConductor);

        if (reservaOpt.isPresent() && conductorOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            Conductor conductor = conductorOpt.get();
            reserva.setConductor(conductor);
            reservaRepository.save(reserva);
        }

        return "redirect:/admin/reserva/detalle/" + idReserva;
    }
 
    @GetMapping("/reservas/{id}/word")
    public void descargarReservaWord(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<Reserva> reservaOpt = reservaRepository.findById(id);
        if (reservaOpt.isEmpty()) {
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
            table.getRow(5).getCell(1)
                    .setText(reserva.getOrigenReserva() != null ? reserva.getOrigenReserva() : "N/A");

            table.getRow(6).getCell(0).setText("Destino");
            table.getRow(6).getCell(1)
                    .setText(reserva.getDestinoReserva() != null ? reserva.getDestinoReserva() : "N/A");

            table.getRow(7).getCell(0).setText("Costo");
            table.getRow(7).getCell(1).setText(String.format("%.2f", reserva.getCostoReserva()));

            ServletOutputStream out = response.getOutputStream();
            document.write(out);
            out.flush();
        }
    }
}
