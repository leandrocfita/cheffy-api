package br.com.fiap.cheffy.infrastructure.persistence.restaurant.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantJpaEntityTest {

    @Test
    void setAndGetAllFields() {
        RestaurantJpaEntity entity = new RestaurantJpaEntity();
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        entity.setId(id);
        entity.setName("Restaurante");
        entity.setCnpj("27865757000102");
        entity.setCulinary("Brasileira");
        entity.setOpeningTime(LocalTime.of(9, 0));
        entity.setClosingTime(LocalTime.of(18, 0));
        entity.setOpen24hours(false);
        entity.setZoneId("America/Sao_Paulo");
        entity.setActive(true);
        entity.setFoodItems(new HashSet<>());
        entity.setDateCreated(now);
        entity.setLastUpdated(now);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Restaurante");
        assertThat(entity.getCnpj()).isEqualTo("27865757000102");
        assertThat(entity.getCulinary()).isEqualTo("Brasileira");
        assertThat(entity.getOpeningTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(entity.getClosingTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(entity.isOpen24hours()).isFalse();
        assertThat(entity.getZoneId()).isEqualTo("America/Sao_Paulo");
        assertThat(entity.getActive()).isTrue();
        assertThat(entity.getFoodItems()).isEmpty();
        assertThat(entity.getDateCreated()).isEqualTo(now);
        assertThat(entity.getLastUpdated()).isEqualTo(now);
    }
}
