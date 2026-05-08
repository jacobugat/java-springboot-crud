package com.example.crud.controller;

import com.example.crud.model.AuthResponse;
import com.example.crud.model.LoginRequest;
import com.example.crud.repository.UsuarioRepository;
import com.example.crud.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Permite que Angular se conecte
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        return usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    // Verificamos si la contraseña coincide
                    if (encoder.matches(password, usuario.getPassword())) {

                        // 1. Generamos el token JWT directo
                        String token = jwtUtil.generateToken(username);

                        // 2. Devolvemos la respuesta con el nuevo constructor:
                        // Estructura: username, status, token, message
                        return ResponseEntity.ok(new AuthResponse(
                                username,
                                "SUCCESS",
                                token,
                                "Login exitoso"
                        ));
                    } else {
                        // Error 401 si la contraseña no coincide
                        return ResponseEntity.status(401)
                                .body(new AuthResponse(username, "ERROR", null, "Contraseña incorrecta"));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(401)
                        .body(new AuthResponse(username, "ERROR", null, "Usuario no encontrado")));
    }
}