package br.com.fiap.cheffy.infrastructure.persistence.profile.repository;

import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileJpaRepository extends JpaRepository<ProfileJpaEntity, Long> {

    Optional<ProfileJpaEntity> findByType(String type);

}