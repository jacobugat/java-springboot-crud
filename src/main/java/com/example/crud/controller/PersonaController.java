package com.example.crud.controller;

import com.example.crud.model.Persona;
import com.example.crud.repository.PersonaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
// Permitimos explícitamente los headers de autenticación para evitar el 403 en el navegador
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class PersonaController {

    private final PersonaRepository personaRepository;

    // 1. OBTENER TODAS LAS PERSONAS
    @GetMapping
    public ResponseEntity<List<Persona>> listarPersonas() {
        List<Persona> personas = personaRepository.findAll();
        // Usar ResponseEntity ayuda a que Spring maneje mejor la conversión a JSON
        return ResponseEntity.ok(personas);
    }

    // 2. OBTENER UNA PERSONA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Persona> obtenerPorId(@PathVariable Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la persona con id: " + id));
        return ResponseEntity.ok(persona);
    }

    // 3. GUARDAR UNA NUEVA PERSONA (Con limpieza de HTML)
    @PostMapping
    public ResponseEntity<Persona> guardarPersona(@Valid @RequestBody Persona persona) {
        // Limpiamos los datos antes de guardar por seguridad
        persona.setNombre(Jsoup.clean(persona.getNombre(), Safelist.none()));
        persona.setApellido(Jsoup.clean(persona.getApellido(), Safelist.none()));
        Persona nuevaPersona = personaRepository.save(persona);
        return ResponseEntity.ok(nuevaPersona);
    }

    // 4. ACTUALIZAR UNA PERSONA
    @PutMapping("/{id}")
    public ResponseEntity<Persona> actualizarPersona(@PathVariable Long id, @Valid @RequestBody Persona personaActualizada) {
        Persona personaDoc = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe la persona con id: " + id));

        // Limpieza y actualización
        personaDoc.setNombre(Jsoup.clean(personaActualizada.getNombre(), Safelist.none()));
        personaDoc.setApellido(Jsoup.clean(personaActualizada.getApellido(), Safelist.none()));
        personaDoc.setEmail(personaActualizada.getEmail());

        Persona actualizado = personaRepository.save(personaDoc);
        return ResponseEntity.ok(actualizado);
    }

    // 5. ELIMINAR UNA PERSONA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Long id) {
        personaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}