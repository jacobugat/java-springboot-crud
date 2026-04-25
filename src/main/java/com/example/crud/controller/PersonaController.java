package com.example.crud.controller;

import com.example.crud.model.Persona;
import com.example.crud.repository.PersonaRepository;
import jakarta.validation.Valid; // Importante para que funcione el validador
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Listo para conectar con Angular después
public class PersonaController {

    private final PersonaRepository personaRepository;

    // 1. OBTENER TODAS LAS PERSONAS (READ)
    @GetMapping
    public List<Persona> listarPersonas() {
        return personaRepository.findAll();
    }

    // 2. OBTENER UNA PERSONA POR ID (READ)
    @GetMapping("/{id}")
    public Persona obtenerPorId(@PathVariable Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la persona con id: " + id));
    }

    // 3. GUARDAR UNA NUEVA PERSONA (CREATE) - Ahora con @Valid
    @PostMapping
    public Persona guardarPersona(@Valid @RequestBody Persona persona) {
        return personaRepository.save(persona);
    }

    // 4. ACTUALIZAR UNA PERSONA EXISTENTE (UPDATE) - Ahora con @Valid
    @PutMapping("/{id}")
    public Persona actualizarPersona(@PathVariable Long id, @Valid @RequestBody Persona personaActualizada) {
        Persona personaDoc = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la persona con id: " + id));

        personaDoc.setNombre(personaActualizada.getNombre());
        personaDoc.setApellido(personaActualizada.getApellido());
        personaDoc.setEmail(personaActualizada.getEmail());

        return personaRepository.save(personaDoc);
    }

    // 5. ELIMINAR UNA PERSONA (DELETE)
    @DeleteMapping("/{id}")
    public void eliminarPersona(@PathVariable Long id) {
        personaRepository.deleteById(id);
    }
}