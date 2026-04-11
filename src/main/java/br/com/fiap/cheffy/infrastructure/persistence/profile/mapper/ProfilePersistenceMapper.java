package br.com.fiap.cheffy.infrastructure.persistence.profile.mapper;

import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfilePersistenceMapper {

    public ProfileJpaEntity toJpaReference(Profile profile) {
        ProfileJpaEntity jpa = new ProfileJpaEntity();
        jpa.setId(profile.getId());
        jpa.setType(profile.getType());
        return jpa;
    }

    public Profile toDomain(ProfileJpaEntity jpa) {
        return Profile.create(jpa.getId(), jpa.getType());
    }
}
