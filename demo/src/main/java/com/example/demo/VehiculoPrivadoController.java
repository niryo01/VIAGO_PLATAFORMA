package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VehiculoPrivadoController {
    @GetMapping("/vehiculoPrivado")
    public String showIndexInPage() {
        return "vehiculoPrivado";
    }
    
}
