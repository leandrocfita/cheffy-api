package br.com.fiap.cheffy.domain.restaurant.entity;

import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.valueobject.WorkingHours;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestaurantTest {

    @Test
    void createRestaurantSetsOwnerAndValidState() {
        User owner = new User(UUID.randomUUID(), "Owner", "owner@mail.com", "owner", "Password@1234", true);
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                owner
        );

        assertThat(restaurant.getOwner()).isEqualTo(owner);
        assertThat(restaurant.isActive()).isTrue();
        assertThat(restaurant.getMenu().getItems()).isEmpty();
    }

    @Test
    void create24hRestaurantSetsOwnerAndValidState() {
        User owner = new User(UUID.randomUUID(), "Owner", "owner@mail.com", "owner", "Password@1234", true);
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        Restaurant restaurant = Restaurant.create24h(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                owner
        );

        assertThat(restaurant.getOwner()).isEqualTo(owner);
        assertThat(restaurant.isActive()).isTrue();
        assertThat(restaurant.getMenu().getItems()).isEmpty();
        assertThat(restaurant.isOpen24hours()).isTrue();
        assertThat(restaurant.getOpeningTime()).isNull();
        assertThat(restaurant.getClosingTime()).isNull();
    }

    @Test
    void createRestaurantThrowsWhenWorkingTimesAreEqual() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        assertThrows(UserOperationNotAllowedException.class, () -> Restaurant.createWithWorkingHours(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("09:00"),
                null
        ));
    }

    @Test
    void createRestaurantThrowsWhenWorkingTimeIsLessThanOneHour() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        assertThrows(UserOperationNotAllowedException.class, () -> Restaurant.createWithWorkingHours(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("09:30"),
                null
        ));
    }

    @Test
    void addAndRemoveFoodItemUpdatesRelations() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                null
        );
        FoodItem item = FoodItem.reconstitute(UUID.randomUUID(), "Prato", "Desc", BigDecimal.TEN, "key", true, true, true);

        restaurant.addFoodItem(item);
        assertThat(item.getRestaurant()).isEqualTo(restaurant);
        assertThat(restaurant.getMenu().getItems()).contains(item);

        restaurant.removeFoodItem(item.getId());
        assertThat(item.getRestaurant()).isNull();
        assertThat(restaurant.getMenu().getItems()).doesNotContain(item);
    }

    @Test
    void addAddressAttachesRestaurantAddress() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                null
        );
        Address address = new Address(1L, "Rua A", 10, "São Paulo", "01001000", "Centro", "SP", "Casa", true);

        restaurant.addAddress(address);

        assertThat(restaurant.getAddress()).isEqualTo(address);
    }

    @Test
    void shouldReturnTrueWhenOpenAtGivenInstant() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                null
        );

        Instant instant = ZonedDateTime
                .of(2024, 1, 1, 10, 0, 0, 0, zoneId)
                .toInstant();

        assertThat(restaurant.isOpenAt(instant)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenOutsideWorkingHours() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                null
        );

        Instant instant = ZonedDateTime
                .of(2024, 1, 1, 20, 0, 0, 0, zoneId)
                .toInstant();

        assertThat(restaurant.isOpenAt(instant)).isFalse();
    }

    @Test
    void shouldHandleCrossingMidnight() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("22:00"),
                LocalTime.parse("06:00"),
                null
        );

        Instant instantInside = ZonedDateTime
                .of(2024, 1, 1, 23, 0, 0, 0, zoneId)
                .toInstant();

        Instant instantAfterMidnight = ZonedDateTime
                .of(2024, 1, 2, 2, 0, 0, 0, zoneId)
                .toInstant();

        Instant instantOutside = ZonedDateTime
                .of(2024, 1, 1, 21, 0, 0, 0, zoneId)
                .toInstant();

        assertThat(restaurant.isOpenAt(instantInside)).isTrue();
        assertThat(restaurant.isOpenAt(instantAfterMidnight)).isTrue();
        assertThat(restaurant.isOpenAt(instantOutside)).isFalse();
    }

    @Test
    void shouldAlwaysReturnTrueWhen24Hours() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        Restaurant restaurant = Restaurant.create24h(
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                null
        );

        Instant instant = ZonedDateTime
                .of(2024, 1, 1, 3, 0, 0, 0, zoneId)
                .toInstant();

        assertThat(restaurant.isOpenAt(instant)).isTrue();
    }

    @Test
    void shouldReturnFalseWhenRestaurantIsInactive() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

        Restaurant restaurant = Restaurant.reconstitute(
                UUID.randomUUID(),
                "Restaurante",
                "27865757000102",
                "Brasileira",
                zoneId,
                LocalTime.parse("09:00"),
                LocalTime.parse("18:00"),
                false,
                false, // inactive
                null,
                null,
                new Menu(new java.util.HashSet<>())
        );

        Instant instant = ZonedDateTime
                .of(2024, 1, 1, 10, 0, 0, 0, zoneId)
                .toInstant();

        assertThat(restaurant.isOpenAt(instant)).isFalse();
    }

    @Test
    void isOpenNowDelegatesToIsOpenAt() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.create24h("R", "27865757000102", "Italiana", zoneId, null);
        assertThat(restaurant.isOpenNow()).isTrue();
    }

    @Test
    void deactivateSetsActiveToFalse() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.create24h("R", "27865757000102", "Italiana", zoneId, null);

        restaurant.deactivate();

        assertThat(restaurant.isActive()).isFalse();
    }

    @Test
    void deactivateThrowsWhenAlreadyInactive() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.reconstitute(
                UUID.randomUUID(), "R", "27865757000102", "Italiana", zoneId,
                null, null, true, false, null, null, new Menu(new java.util.HashSet<>()));

        assertThrows(RestaurantOperationNotAllowedException.class, restaurant::deactivate);
    }

    @Test
    void reactivateSetsActiveToTrue() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.reconstitute(
                UUID.randomUUID(), "R", "27865757000102", "Italiana", zoneId,
                null, null, true, false, null, null, new Menu(new java.util.HashSet<>()));

        restaurant.reactivate();

        assertThat(restaurant.isActive()).isTrue();
    }

    @Test
    void reactivateThrowsWhenAlreadyActive() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.create24h("R", "27865757000102", "Italiana", zoneId, null);

        assertThrows(RestaurantOperationNotAllowedException.class, restaurant::reactivate);
    }

    @Test
    void isOwnedByUserReturnsTrueWhenOwnerIsActiveAndHasOwnerProfile() {
        UUID userId = UUID.randomUUID();
        User owner = new User(userId, "Owner", "owner@mail.com", "owner", "Password@1234", true);
        owner.addProfile(Profile.create(1L, ProfileType.OWNER.name()));
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.create24h("R", "27865757000102", "Italiana", zoneId, owner);

        assertThat(restaurant.isOwnedByUser(userId)).isTrue();
    }

    @Test
    void isOwnedByUserReturnsFalseWhenDifferentUserId() {
        UUID userId = UUID.randomUUID();
        User owner = new User(userId, "Owner", "owner@mail.com", "owner", "Password@1234", true);
        owner.addProfile(Profile.create(1L, ProfileType.OWNER.name()));
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.create24h("R", "27865757000102", "Italiana", zoneId, owner);

        assertThat(restaurant.isOwnedByUser(UUID.randomUUID())).isFalse();
    }

    @Test
    void isOwnedByUserReturnsFalseWhenUserIsInactive() {
        UUID userId = UUID.randomUUID();
        User owner = new User(userId, "Owner", "owner@mail.com", "owner", "Password@1234", false);
        owner.addProfile(Profile.create(1L, ProfileType.OWNER.name()));
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.create24h("R", "27865757000102", "Italiana", zoneId, owner);

        assertThat(restaurant.isOwnedByUser(userId)).isFalse();
    }

    @Test
    void isOwnedByUserReturnsFalseWhenUserHasNoOwnerProfile() {
        UUID userId = UUID.randomUUID();
        User owner = new User(userId, "Owner", "owner@mail.com", "owner", "Password@1234", true);
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.create24h("R", "27865757000102", "Italiana", zoneId, owner);

        assertThat(restaurant.isOwnedByUser(userId)).isFalse();
    }

    @Test
    void patchUpdatesNameAndCulinary() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante", "27865757000102", "Brasileira", zoneId,
                LocalTime.parse("09:00"), LocalTime.parse("18:00"), null);

        restaurant.patch("Novo Nome", "Japonesa", null, null);

        assertThat(restaurant.getName()).isEqualTo("Novo Nome");
        assertThat(restaurant.getCulinary()).isEqualTo("Japonesa");
    }
    @Test
    void patchIgnoresNullAndBlankValues() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante", "27865757000102", "Brasileira", zoneId,
                LocalTime.parse("09:00"), LocalTime.parse("18:00"), null);

        restaurant.patch(null, "  ", null, null);

        assertThat(restaurant.getName()).isEqualTo("Restaurante");
        assertThat(restaurant.getCulinary()).isEqualTo("Brasileira");
    }
    @Test
    void patchUpdatesZoneId() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante", "27865757000102", "Brasileira", zoneId,
                LocalTime.parse("09:00"), LocalTime.parse("18:00"), null);
        ZoneId newZoneId = ZoneId.of("Europe/London");

        restaurant.patch(null, null, newZoneId, null);

        assertThat(restaurant.getZoneId()).isEqualTo(newZoneId);
    }
    @Test
    void patchUpdatesWorkingHours() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.createWithWorkingHours(
                "Restaurante", "27865757000102", "Brasileira", zoneId,
                LocalTime.parse("09:00"), LocalTime.parse("18:00"), null);
        WorkingHours newHours = WorkingHours.of(LocalTime.parse("10:00"), LocalTime.parse("22:00"));

        restaurant.patch(null, null, null, newHours);

        assertThat(restaurant.getOpeningTime()).isEqualTo(LocalTime.parse("10:00"));
        assertThat(restaurant.getClosingTime()).isEqualTo(LocalTime.parse("22:00"));
    }
    @Test
    void patchThrowsWhenRestaurantIsInactive() {
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Restaurant restaurant = Restaurant.reconstitute(
                UUID.randomUUID(), "R", "27865757000102", "Italiana", zoneId,
                null, null, true, false, null, null, new Menu(new java.util.HashSet<>()));
        assertThrows(RestaurantOperationNotAllowedException.class,
                () -> restaurant.patch("Novo Nome", null, null, null));
    }
}
