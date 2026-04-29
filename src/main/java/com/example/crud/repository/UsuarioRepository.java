package com.example.crud.repository;

import com.example.crud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Esta línea permite buscar por nombre de usuario en la base de datos
    Optional<Usuario> findByUsername(String username);
}