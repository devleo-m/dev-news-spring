package br.com.tech.repository;

import br.com.tech.entity.PapelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PapelRepository extends JpaRepository<PapelEntity, UUID> {
    Optional<PapelEntity> findByNome(String nome);
}
