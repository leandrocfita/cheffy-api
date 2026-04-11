package br.com.fiap.cheffy.infrastructure.persistence.profile.mapper;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.infrastructure.persistence.profile.entity.ProfileJpaEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProfilePersistenceMapperTest {

    private final ProfilePersistenceMapper mapper = new ProfilePersistenceMapper();

    @Test
    void toDomainMapsJpaEntityToProfile() {
        ProfileJpaEntity entity = new ProfileJpaEntity();
        entity.setId(1L);
        entity.setType(ProfileType.CLIENT.getType());

        Profile result = mapper.toDomain(entity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getType()).isEqualTo(ProfileType.CLIENT.getType());
    }

    @Test
    void toJpaReferenceMapsProfileToJpaEntity() {
        Profile profile = Profile.create(2L, "OWNER");

        ProfileJpaEntity result = mapper.toJpaReference(profile);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
    }
}
