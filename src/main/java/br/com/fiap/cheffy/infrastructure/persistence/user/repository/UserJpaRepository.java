package br.com.fiap.cheffy.infrastructure.persistence.user.repository;

import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    List<UserJpaEntity> findAll();

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    Optional<UserJpaEntity> findByEmail(@Param("email") String email);

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    Optional<UserJpaEntity> findById(@Param("id") UUID id);

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    Page<UserJpaEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"profiles", "addresses"})
    Page<UserJpaEntity> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
}