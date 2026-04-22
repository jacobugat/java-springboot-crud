package com.example.crud.repository;

import com.example.crud.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    // Magia: Aquí ya tienes métodos como .save(), .findAll(), .deleteById() sin escribir nada.
}