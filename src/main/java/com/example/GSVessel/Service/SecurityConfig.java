package com.example.GSVessel.Service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UserDetailService userDetailsService;

    public SecurityConfig(UserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Bean para encriptar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para manejar autenticación
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    // Configuración de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Deshabilitamos CSRF para API REST
                .authorizeHttpRequests()
                .requestMatchers("/users/register").permitAll() // ruta de registro abierta
                .requestMatchers("/admin/**").hasRole("ADMIN") // rutas de admin
                .anyRequest().authenticated() // el resto requiere login
                .and()
                .httpBasic(); // autenticación básica (puede cambiarse a JWT después)
        return http.build();
    }
}
