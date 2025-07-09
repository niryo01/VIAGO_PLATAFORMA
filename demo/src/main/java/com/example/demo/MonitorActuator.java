package com.example.demo;



import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MonitorActuator {

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 300000) // Cada 5 minutos (300000 milisegundos)
    public void checkHealthAndMetrics() {
        String health = restTemplate.getForObject("http://localhost:8080/actuator/health", String.class);
        String cpuUsage = restTemplate.getForObject("http://localhost:8080/actuator/metrics/system.cpu.usage", String.class);

        System.out.println("HEALTH : " + health);
        System.out.println("CPU USAGE: " + cpuUsage);
        System.out.println("---- Consulta realizada automáticamente ----");
    }
}
