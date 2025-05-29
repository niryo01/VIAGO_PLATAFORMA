package com.example.demo;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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



    // ESTE METODO USA APACHE POI, ES PARA DESCARGAR LA RESERVA DEL VIAJE EN FORMATO WORD
    @GetMapping("/reservas/{id}/word")
public void descargarReservaWord(@PathVariable String id, HttpServletResponse response) throws IOException {
    Reserva reserva = reservas.stream()
                             .filter(r -> r.getIdReserva().equals(id))
                             .findFirst()
                             .orElse(null);

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
        table.getRow(0).getCell(1).setText(reserva.getIdReserva() != null ? reserva.getIdReserva() : "");

        table.getRow(1).getCell(0).setText("ID Usuario");
        table.getRow(1).getCell(1).setText(reserva.getIdUsuario() != null ? reserva.getIdUsuario() : "");

        table.getRow(2).getCell(0).setText("ID Conductor");
        table.getRow(2).getCell(1).setText(reserva.getIdConductor() != null ? reserva.getIdConductor() : "");

        table.getRow(3).getCell(0).setText("Fecha Reserva");
        table.getRow(3).getCell(1).setText(reserva.getFechaReserva() != null ? reserva.getFechaReserva() : "");

        table.getRow(4).getCell(0).setText("Hora Reserva");
        table.getRow(4).getCell(1).setText(reserva.getHoraReserva() != null ? reserva.getHoraReserva() : "");

        table.getRow(5).getCell(0).setText("Origen");
        table.getRow(5).getCell(1).setText(reserva.getOrigenReserva() != null ? reserva.getOrigenReserva() : "");

        table.getRow(6).getCell(0).setText("Destino");
        table.getRow(6).getCell(1).setText(reserva.getDestinoReserva() != null ? reserva.getDestinoReserva() : "");

        table.getRow(7).getCell(0).setText("Costo");
        table.getRow(7).getCell(1).setText(String.format("%.2f", reserva.getCostoReserva()));

        ServletOutputStream out = response.getOutputStream();
        document.write(out);
        out.flush();
    }
}

}

