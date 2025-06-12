package com.example.demo;

import com.example.demo.dao.ReservaDAO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
public class ReservaController {

    @Autowired
    private ReservaDAO reservaDAO; // ✅ Inyectamos el DAO

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

        reservaDAO.agregar(nuevaReserva); // ✅ Se agrega usando el DAO
        session.setAttribute("misReservas", reservaDAO.obtenerReservasPorUsuario(usuario.getIdUsuario()));

        return "redirect:/viajes";
    }

    @GetMapping("/viajes")
    public String showViajes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        List<Reserva> reservasUsuario = reservaDAO.obtenerReservasPorUsuario(usuario.getIdUsuario());
        model.addAttribute("reservas", reservasUsuario);
        return "viajes";
    }

    @PostMapping("/admin/reserva/asignarConductor")
public String asignarConductor(@RequestParam String idReserva,
                               @RequestParam String idConductor) {
    reservaDAO.asignarConductor(idReserva, idConductor); // lógica de memoria
    return "redirect:/admin/reserva/detalle/" + idReserva;
}

    // ✅ Exportar reserva a Word
    @GetMapping("/reservas/{id}/word")
    public void descargarReservaWord(@PathVariable String id, HttpServletResponse response) throws IOException {
        Reserva reserva = reservaDAO.obtenerPorId(id); // ✅ Usamos DAO

        if (reserva == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=Reserva_" + reserva.getIdReserva() + ".docx");

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
            table.getRow(0).getCell(1).setText(reserva.getIdReserva());

            table.getRow(1).getCell(0).setText("ID Usuario");
            table.getRow(1).getCell(1).setText(reserva.getIdUsuario());

            table.getRow(2).getCell(0).setText("ID Conductor");
            table.getRow(2).getCell(1).setText(reserva.getIdConductor());

            table.getRow(3).getCell(0).setText("Fecha Reserva");
            table.getRow(3).getCell(1).setText(reserva.getFechaReserva());

            table.getRow(4).getCell(0).setText("Hora Reserva");
            table.getRow(4).getCell(1).setText(reserva.getHoraReserva());

            table.getRow(5).getCell(0).setText("Origen");
            table.getRow(5).getCell(1).setText(reserva.getOrigenReserva());

            table.getRow(6).getCell(0).setText("Destino");
            table.getRow(6).getCell(1).setText(reserva.getDestinoReserva());

            table.getRow(7).getCell(0).setText("Costo");
            table.getRow(7).getCell(1).setText(String.format("%.2f", reserva.getCostoReserva()));

            ServletOutputStream out = response.getOutputStream();
            document.write(out);
            out.flush();
        }
    }
}
