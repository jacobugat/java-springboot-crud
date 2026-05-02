package com.example.crud.controller;

import com.example.crud.model.AuthResponse;
import com.example.crud.model.LoginRequest;
import com.example.crud.model.Usuario;
import com.example.crud.repository.UsuarioRepository;
import com.example.crud.service.UsuarioService;
import com.example.crud.security.JwtUtil; // 1. IMPORTANTE: Verifica que esta ruta coincida con tu carpeta
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

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
                    if (encoder.matches(password, usuario.getPassword())) {
                        if (usuario.isMfaEnabled()) {
                            return ResponseEntity.ok(new AuthResponse(username, "MFA_REQUIRED", "Código requerido"));
                        }
                        // Si no tiene MFA, generamos token directo
                        String token = jwtUtil.generateToken(username);
                        return ResponseEntity.ok(new AuthResponse(username, "SUCCESS", token));
                    }
                    return ResponseEntity.status(401).body("Credenciales inválidas");
                })
                .orElseGet(() -> ResponseEntity.status(401).body("Usuario no encontrado"));
    }

    @GetMapping("/validar-mfa/{username}/{codigo}")
    public ResponseEntity<?> validarMFA(@PathVariable String username, @PathVariable int codigo) {
        return usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    boolean esValido = usuarioService.verificarCodigoMFA(usuario.getMfaSecret(), codigo);
                    if (esValido) {
                        // ESTA ES LA LÍNEA CLAVE: Generar el token
                        String token = jwtUtil.generateToken(username);
                        // Y enviarlo aquí en el tercer parámetro
                        return ResponseEntity.ok(new AuthResponse(username, "SUCCESS", token));
                    }
                    return ResponseEntity.status(401).body("Código inválido");
                })
                .orElse(ResponseEntity.status(404).body("Usuario no encontrado"));
    }
}