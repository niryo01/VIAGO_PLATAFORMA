package com.example.demo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    //BEAN PARA SEGURIDAD, ESTO PARA EVITAR FILTRO DE CONTRASEÑAS EN CASO DE ROBO DE BASE DE DATOS
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/logout", "/css/**", "/js/**", "/images/**", "/styles.css", "/").permitAll()
            .anyRequest().permitAll()
        )
        .formLogin(login -> login.disable())
        .build();
}

    
}