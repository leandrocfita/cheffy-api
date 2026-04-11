package br.com.fiap.cheffy.infrastructure.persistence.profile.entity;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileJpaEntityTest {

    @Test
    void createProfileJpaEntity() {
        ProfileJpaEntity entity = new ProfileJpaEntity();
        entity.setId(1L);
        entity.setType("cliente");

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getType()).isEqualTo("cliente");
    }

    @Test
    void setAndGetDateCreated() {
        ProfileJpaEntity entity = new ProfileJpaEntity();
        OffsetDateTime now = OffsetDateTime.now();
        entity.setDateCreated(now);

        assertThat(entity.getDateCreated()).isEqualTo(now);
    }

    @Test
    void setAndGetLastUpdated() {
        ProfileJpaEntity entity = new ProfileJpaEntity();
        OffsetDateTime now = OffsetDateTime.now();
        entity.setLastUpdated(now);

        assertThat(entity.getLastUpdated()).isEqualTo(now);
    }
}
