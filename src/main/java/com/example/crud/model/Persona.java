package com.example.crud.model; // Asegúrate que sea tu paquete real

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "persona") // Esto le dice a Java que use la tabla 'persona' en MySQL
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;
}