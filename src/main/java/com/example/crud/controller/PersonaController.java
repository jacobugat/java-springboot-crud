package com.example.crud.controller;

import com.example.crud.model.Persona;
import com.example.crud.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor // Genera el constructor para los atributos 'final'
@CrossOrigin(origins = "http://localhost:4200") // Esto permitirá que Angular se conecte luego
public class PersonaController {

    // Usamos 'final' para que la inyección sea por constructor (lo recomendado)
    private final PersonaRepository personaRepository;

    // Obtener la lista de todas las personas
    @GetMapping
    public List<Persona> listarPersonas() {
        return personaRepository.findAll();
    }

    // Guardar una nueva persona
    @PostMapping
    public Persona guardarPersona(@RequestBody Persona persona) {
        return personaRepository.save(persona);
    }

    // Buscar una persona por ID (Extra para tu CRUD)
    @GetMapping("/{id}")
    public Persona obtenerPorId(@PathVariable Long id) {
        return personaRepository.findById(id).orElse(null);
    }

    // Eliminar una persona (Extra para tu CRUD)
    @DeleteMapping("/{id}")
    public void eliminarPersona(@PathVariable Long id) {
        personaRepository.deleteById(id);
    }
}