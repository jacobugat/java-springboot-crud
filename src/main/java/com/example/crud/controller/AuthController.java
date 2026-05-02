package com.example.crud.controller;

import com.example.crud.model.AuthResponse;
import com.example.crud.model.LoginRequest; // Importamos el nuevo modelo
import com.example.crud.model.Usuario;
import com.example.crud.repository.UsuarioRepository;
import com.example.crud.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
// El CORS ya lo manejamos en SecurityConfig, así que aquí no es estrictamente necesario
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil; // Inyectamos la utilidad

    @GetMapping("/validar-mfa/{username}/{codigo}")
    public ResponseEntity<?> validarMFA(@PathVariable String username, @PathVariable int codigo) {
        return usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    boolean esValido = usuarioService.verificarCodigoMFA(usuario.getMfaSecret(), codigo);
                    if (esValido) {
                        // GENERAMOS EL TOKEN AQUÍ
                        String token = jwtUtil.generateToken(username);
                        return ResponseEntity.ok(new AuthResponse(username, "SUCCESS", token));
                    }
                    return ResponseEntity.status(401).body("Código inválido");
                })
                .orElse(ResponseEntity.status(404).body("Usuario no encontrado"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // Debug para ver en la consola de IntelliJ
        System.out.println("Intento de login para: " + username);
        System.out.println("NUEVO HASH PARA LA DB: " + encoder.encode("123456"));

        return usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    // Verificación técnica de BCrypt
                    if (encoder.matches(password, usuario.getPassword())) {

                        if (usuario.isMfaEnabled()) {
                            return ResponseEntity.ok(new AuthResponse(username, "MFA_REQUIRED", "Código requerido"));
                        }

                        return ResponseEntity.ok(new AuthResponse(username, "SUCCESS", "Login directo exitoso"));
                    }
                    System.out.println("Password incorrecto para: " + username);
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
                        return ResponseEntity.ok(new AuthResponse(username, "SUCCESS", "MFA Correcto"));
                    }
                    return ResponseEntity.status(401).body("Código inválido");
                })
                .orElse(ResponseEntity.status(404).body("Usuario no encontrado"));
    }
}