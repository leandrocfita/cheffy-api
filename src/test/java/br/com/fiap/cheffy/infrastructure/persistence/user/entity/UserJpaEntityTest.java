//package br.com.fiap.cheffy.infrastructure.persistence.user.entity;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.OffsetDateTime;
//import java.util.HashSet;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class UserJpaEntityTest {
//
//    @Test
//    void createUserJpaEntity() {
//        UserJpaEntity entity = new UserJpaEntity();
//        entity.setId(UUID.randomUUID());
//        entity.setName("Name");
//        entity.setEmail("email@test.com");
//        entity.setLogin("login");
//        entity.setPassword("pass");
//        entity.setProfiles(new HashSet<>());
//        entity.setAddresses(new HashSet<>());
//
//        assertThat(entity.getId()).isNotNull();
//        assertThat(entity.getName()).isEqualTo("Name");
//        assertThat(entity.getEmail()).isEqualTo("email@test.com");
//        assertThat(entity.getLogin()).isEqualTo("login");
//        assertThat(entity.getPassword()).isEqualTo("pass");
//        assertThat(entity.getProfiles()).isNotNull();
//        assertThat(entity.getAddresses()).isNotNull();
//    }
//
//    @Test
//    void setAndGetDateCreated() {
//        UserJpaEntity entity = new UserJpaEntity();
//        OffsetDateTime now = OffsetDateTime.now();
//        entity.setDateCreated(now);
//
//        assertThat(entity.getDateCreated()).isEqualTo(now);
//    }
//
//    @Test
//    void setAndGetLastUpdated() {
//        UserJpaEntity entity = new UserJpaEntity();
//        OffsetDateTime now = OffsetDateTime.now();
//        entity.setLastUpdated(now);
//
//        assertThat(entity.getLastUpdated()).isEqualTo(now);
//    }
//
//    @Test
//    void setAndGetActive() {
//        UserJpaEntity entity = new UserJpaEntity();
//        entity.setActive(true);
//        assertThat(entity.isActive()).isTrue();
//        entity.setActive(false);
//        assertThat(entity.isActive()).isFalse();
//    }
//}
