package com.example.crud.config;

import com.example.crud.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configuración de CORS vinculada al Bean corsConfigurationSource
                .cors(Customizer.withDefaults())

                // 2. Deshabilitar CSRF (Innecesario para APIs Stateless con JWT)
                .csrf(csrf -> csrf.disable())

                // 3. Gestión de sesión STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Reglas de autorización (Paso 4 modificado)
                .authorizeHttpRequests(auth -> auth
                        // REFUERZO: Permitir explícitamente peticiones OPTIONS para evitar el 403 de Preflight
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Endpoints públicos de autenticación y registro
                        .requestMatchers("/api/auth/**").permitAll()
                        // El resto de la API requiere el token Bearer
                        .anyRequest().authenticated()
                )

                // 5. Filtro de JWT antes del filtro de usuario/contraseña
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración detallada de CORS para el intercambio de datos con Angular.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Origen de tu aplicación Angular
        config.setAllowedOrigins(List.of("http://localhost:4200"));

        // Métodos permitidos para el CRUD
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers permitidos: Añadimos 'Accept' para mayor compatibilidad
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Cache-Control", "X-Requested-With", "Accept"));

        // CRÍTICO: Exponer el header Authorization para que el navegador no lo bloquee al recibir respuestas
        config.setExposedHeaders(Arrays.asList("Authorization"));

        // Permite credenciales (Cookies/Auth)
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}