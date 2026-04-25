package com.example.crud.model;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Column(unique = true)
    private String email;

    // --- ESCUDO 1: SETTERS MANUALES (Para limpiar antes de responder a Postman) ---

    public void setNombre(String nombre) {
        this.nombre = nombre != null ? Jsoup.clean(nombre, Safelist.none()) : null;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido != null ? Jsoup.clean(apellido, Safelist.none()) : null;
    }

    public void setEmail(String email) {
        this.email = email != null ? Jsoup.clean(email, Safelist.none()) : null;
    }

    // --- ESCUDO 2: PRE-PERSIST (Doble seguridad antes de entrar a MySQL) ---

    @PrePersist
    @PreUpdate
    public void asegurarLimpieza() {
        if (this.nombre != null) this.nombre = Jsoup.clean(this.nombre, Safelist.none());
        if (this.apellido != null) this.apellido = Jsoup.clean(this.apellido, Safelist.none());
        if (this.email != null) this.email = Jsoup.clean(this.email, Safelist.none());
    }
}