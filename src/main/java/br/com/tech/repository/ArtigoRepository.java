package br.com.tech.repository;

import br.com.tech.entity.ArtigoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtigoRepository extends JpaRepository<ArtigoEntity, Long> {
    Optional<ArtigoEntity> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);
}
