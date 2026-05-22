package br.com.fiap.cheffy.domain.user.entity;

import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.user.exception.AddressNotFoundException;
import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    void createUserWithProfile() {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());

        User user = User.create("John", "john@test.com", profile);

        assertThat(user.getName()).isEqualTo("John");
        assertThat(user.getEmail()).isEqualTo("john@test.com");
        assertThat(user.getProfiles()).contains(profile);
    }

    @Test
    void patchUpdatesFields() {
        User user = new User(UUID.randomUUID(), "Old", "old@test.com", "some-auth-id", true, AuthStatus.CONFIRMED);

        user.patch("New", "new@test.com");

        assertThat(user.getName()).isEqualTo("New");
        assertThat(user.getEmail()).isEqualTo("new@test.com");
    }

    @Test
    void patchWithNullsDoesNotUpdate() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);

        user.patch(null, null);

        assertThat(user.getName()).isEqualTo("Name");
        assertThat(user.getEmail()).isEqualTo("email@test.com");
        assertThat(user.getAuthStatus()).isEqualTo(AuthStatus.CONFIRMED);
    }

    @Test
    void addAddressAttachesToUser() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "some-auth-id", true, AuthStatus.CONFIRMED);
        Address address = Address.create("Street", 1, "City", "12345678", "Hood", "ST", null, false);

        user.addAddress(address);

        assertThat(user.getAddresses()).hasSize(1);
        assertThat(address.getUser()).isEqualTo(user);
    }

    @Test
    void addMainAddressUnsetsOtherMain() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "some-auth-id",  true, AuthStatus.CONFIRMED);
        Address addr1 = Address.create("St1", 1, "City", "12345678", "Hood", "ST", null, true);
        Address addr2 = Address.create("St2", 2, "City", "87654321", "Hood", "ST", null, true);

        user.addAddress(addr1);
        user.addAddress(addr2);

        assertThat(addr1.isMain()).isFalse();
        assertThat(addr2.isMain()).isTrue();
    }

    @Test
    void removeAddressThrowsIfLastAddress() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login",  true, AuthStatus.CONFIRMED);
        Address address = Address.create("Street", 1, "City", "12345678", "Hood", "ST", null, true);
        user.addAddress(address);

        assertThrows(UserOperationNotAllowedException.class, () -> user.removeAddress(address));
    }

    @Test
    void removeMainAddressSetsNextAsMain() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);
        Address addr1 = Address.create("St1", 1, "City", "12345678", "Hood", "ST", null, true);
        Address addr2 = Address.create("St2", 2, "City", "87654321", "Hood", "ST", null, false);
        user.addAddress(addr1);
        user.addAddress(addr2);

        user.removeAddress(addr1);

        assertThat(user.getAddresses()).hasSize(1);
        assertThat(addr2.isMain()).isTrue();
    }

    @Test
    void removeAddressThrowsIfNotOwned() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);
        Address address = Address.create("Street", 1, "City", "12345678", "Hood", "ST", null, true);

        assertThrows(AddressNotFoundException.class, () -> user.removeAddress(address));
    }

    @Test
    void updateAddressThrowsIfMainSetToFalse() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);
        Address address = new Address(1L, "Old", 1, "City", "12345678", "Hood", "ST", null, true);
        user.addAddress(address);

        assertThrows(UserOperationNotAllowedException.class,
            () -> user.updateAddress(1L, null, null, null, null, null, null, null, false));
    }

    @Test
    void updateAddressUpdatesFields() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);
        Address address = new Address(1L, "Old", 1, "City", "12345678", "Hood", "ST", null, true);
        user.addAddress(address);

        user.updateAddress(1L, "New", 2, "NewCity", "87654321", "NewHood", "NS", "Apt", null);

        assertThat(address.getStreetName()).isEqualTo("New");
        assertThat(address.getNumber()).isEqualTo(2);
    }


    @Test
    void addProfileAddsToSet() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());

        user.addProfile(profile);

        assertThat(user.getProfiles()).contains(profile);
    }

    @Test
    void removeProfileRemovesFromSet() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);

        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());
        user.addProfile(profile);

        user.removeProfile(profile);

        assertThat(user.getProfiles()).doesNotContain(profile);
    }

    @Test
    void setMainAddressUnsetsOthers() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);

        Address addr1 = Address.create("St1", 1, "City", "12345678", "Hood", "ST", null, true);
        Address addr2 = Address.create("St2", 2, "City", "87654321", "Hood", "ST", null, false);
        user.addAddress(addr1);
        user.addAddress(addr2);

        user.setMainAddress(addr2);

        assertThat(addr1.isMain()).isFalse();
        assertThat(addr2.isMain()).isTrue();
    }

    @Test
    void findAddressByIdOrFailThrowsIfNotFound() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);


        assertThrows(AddressNotFoundException.class, () -> user.findAddressByIdOrFail(999L));
    }

    @Test
    void protectedConstructorExists() throws Exception {
        var constructor = User.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        User user = constructor.newInstance();
        assertThat(user).isNotNull();
    }

    @Test
    void deactivateSetsActiveFalse() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", true, AuthStatus.CONFIRMED);

        assertThat(user.isActive()).isTrue();

        user.deactivate();

        assertThat(user.isActive()).isFalse();
    }

    @Test
    void isActiveReturnsFalseWhenCreatedInactive() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", false, AuthStatus.CONFIRMED);

        assertThat(user.isActive()).isFalse();
    }

    @Test
    void activateSetsActiveTrue() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", false, AuthStatus.CONFIRMED);

        assertThat(user.isActive()).isFalse();

        user.activate();

        assertThat(user.isActive()).isTrue();
    }
}
