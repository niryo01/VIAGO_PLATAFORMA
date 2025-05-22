package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class InicioController {

    @GetMapping("/index")
    public String showIndexInPage() {
        return "index";
    }
    
    
}
