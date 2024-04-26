package br.com.tech.repository;

import br.com.tech.entity.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Long> {
    Optional<ComentarioEntity> findById(Long id);
    boolean existsById(Long id);
    void deleteById(Long id);
}