package br.com.fiap.cheffy.infrastructure.persistence.address.entity;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AddressJpaEntityTest {

    @Test
    void createAddressJpaEntity() {
        AddressJpaEntity entity = new AddressJpaEntity();
        entity.setId(1L);
        entity.setStreetName("Street");
        entity.setNumber(123);
        entity.setCity("City");
        entity.setPostalCode("12345678");
        entity.setNeighborhood("Hood");
        entity.setStateProvince("ST");
        entity.setAddressLine("Apt 1");
        entity.setMain(true);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getStreetName()).isEqualTo("Street");
        assertThat(entity.getNumber()).isEqualTo(123);
        assertThat(entity.getCity()).isEqualTo("City");
        assertThat(entity.getPostalCode()).isEqualTo("12345678");
        assertThat(entity.getNeighborhood()).isEqualTo("Hood");
        assertThat(entity.getStateProvince()).isEqualTo("ST");
        assertThat(entity.getAddressLine()).isEqualTo("Apt 1");
        assertThat(entity.getMain()).isTrue();
    }

    @Test
    void setAndGetDateCreated() {
        AddressJpaEntity entity = new AddressJpaEntity();
        OffsetDateTime now = OffsetDateTime.now();
        entity.setDateCreated(now);

        assertThat(entity.getDateCreated()).isEqualTo(now);
    }

    @Test
    void setAndGetLastUpdated() {
        AddressJpaEntity entity = new AddressJpaEntity();
        OffsetDateTime now = OffsetDateTime.now();
        entity.setLastUpdated(now);

        assertThat(entity.getLastUpdated()).isEqualTo(now);
    }
}
