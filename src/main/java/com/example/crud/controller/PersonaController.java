package com.example.crud.controller;

import com.example.crud.model.Persona;
import com.example.crud.repository.PersonaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // <--- PERMISO PARA ANGULAR CONCEDIDO
public class PersonaController {

    private final PersonaRepository personaRepository;

    // 1. OBTENER TODAS LAS PERSONAS
    @GetMapping
    public List<Persona> listarPersonas() {
        return personaRepository.findAll();
    }

    // 2. OBTENER UNA PERSONA POR ID
    @GetMapping("/{id}")
    public Persona obtenerPorId(@PathVariable Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la persona con id: " + id));
    }

    // 3. GUARDAR UNA NUEVA PERSONA (Con limpieza de HTML)
    @PostMapping
    public Persona guardarPersona(@Valid @RequestBody Persona persona) {
        // Limpiamos los datos antes de guardar por seguridad
        persona.setNombre(Jsoup.clean(persona.getNombre(), Safelist.none()));
        persona.setApellido(Jsoup.clean(persona.getApellido(), Safelist.none()));
        return personaRepository.save(persona);
    }

    // 4. ACTUALIZAR UNA PERSONA
    @PutMapping("/{id}")
    public Persona actualizarPersona(@PathVariable Long id, @Valid @RequestBody Persona personaActualizada) {
        Persona personaDoc = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la persona con id: " + id));

        // Limpieza y actualización
        personaDoc.setNombre(Jsoup.clean(personaActualizada.getNombre(), Safelist.none()));
        personaDoc.setApellido(Jsoup.clean(personaActualizada.getApellido(), Safelist.none()));
        personaDoc.setEmail(personaActualizada.getEmail());

        return personaRepository.save(personaDoc);
    }

    // 5. ELIMINAR UNA PERSONA
    @DeleteMapping("/{id}")
    public void eliminarPersona(@PathVariable Long id) {
        personaRepository.deleteById(id);
    }
}