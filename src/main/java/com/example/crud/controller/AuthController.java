package com.example.crud.controller;

import com.example.crud.model.AuthResponse;
import com.example.crud.model.Usuario;
import com.example.crud.service.UsuarioService;
import com.example.crud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // PASO 2: Validar Usuario y Contraseña
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        System.out.println("DEBUG -> Usuario recibido de Angular: [" + username + "]");
        System.out.println("DEBUG -> Password recibido de Angular: [" + password + "]");

        return usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    // Comparamos la clave escrita con la de la DB (BCrypt)
                    if (encoder.matches(password, usuario.getPassword())) {

                        // Si el usuario tiene MFA activo, pedimos el código
                        if (usuario.isMfaEnabled()) {
                            return ResponseEntity.ok(new AuthResponse(username, "MFA_REQUIRED", "Por favor, ingresa el código de tu celular"));
                        }

                        // Si no tiene MFA, entra directo
                        return ResponseEntity.ok(new AuthResponse(username, "SUCCESS", "Login exitoso"));
                    }
                    return ResponseEntity.status(401).body("Contraseña incorrecta");
                })
                .orElse(ResponseEntity.status(401).body("Usuario no encontrado"));
    }

    // PASO 3: Validar el código de 6 dígitos del Authenticator
    @GetMapping("/validar-mfa/{username}/{codigo}")
    public ResponseEntity<?> validarMFA(@PathVariable String username, @PathVariable int codigo) {
        return usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    boolean esValido = usuarioService.verificarCodigoMFA(usuario.getMfaSecret(), codigo);
                    if (esValido) {
                        return ResponseEntity.ok(new AuthResponse(username, "SUCCESS", "Autenticación completada"));
                    }
                    return ResponseEntity.status(401).body("Código de Authenticator inválido o expirado");
                })
                .orElse(ResponseEntity.status(404).body("Usuario no encontrado"));
    }

    // Para ver el QR (Solo se usa la primera vez al configurar)
    @GetMapping("/test-mfa/{username}")
    public String obtenerQrMFA(@PathVariable String username) {
        return usuarioRepository.findByUsername(username)
                .map(usuario -> usuarioService.obtenerQrUrl(usuario))
                .orElse("Usuario no encontrado");
    }
}