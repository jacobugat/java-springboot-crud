package com.example.crud.service;

import com.example.crud.model.Usuario;
import com.example.crud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // Método para registrar o actualizar usuarios con clave cifrada
    public Usuario guardarUsuario(Usuario usuario) {
        // 1. Ciframos la clave antes de que toque la base de datos
        String claveCifrada = encoder.encode(usuario.getPassword());
        usuario.setPassword(claveCifrada);

        // 2. Seteamos la fecha actual para el control de los 3 meses
        usuario.setFechaUltimoCambio(LocalDate.now());

        return repository.save(usuario);
    }

    // Lógica para saber si la clave venció (más de 90 días)
    public boolean debeCambiarClave(Usuario usuario) {
        LocalDate limite = usuario.getFechaUltimoCambio().plusDays(90);
        return LocalDate.now().isAfter(limite);
    }

    // Validación de formato de contraseña
    public void validarPassword(String password) {
        // Esta "fórmula" revisa: mín 8 caracteres, 1 mayúscula, 1 número y 1 símbolo
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

        if (!password.matches(regex)) {
            throw new RuntimeException("La contraseña no cumple con los requisitos de seguridad");
        }
    }
}