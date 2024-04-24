package br.com.tech.repository;

import br.com.tech.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, UUID> {
//    Optional<UsuarioEntity> findByEmail(String email);
//    Optional<UsuarioEntity> findByUsuarioLogin(UUID usuario);
}
