package br.com.fiap.cheffy.domain.user.entity;

import br.com.fiap.cheffy.domain.user.exception.InvalidPostalCodeException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressTest {

    @Test
    void createAddressWithValidPostalCode() {
        Address address = Address.create("Street", 123, "City", "12345678", "Hood", "ST", "Apt 1", true);
        
        assertThat(address.getStreetName()).isEqualTo("Street");
        assertThat(address.getNumber()).isEqualTo(123);
        assertThat(address.getCity()).isEqualTo("City");
        assertThat(address.getPostalCode()).isEqualTo("12345678");
        assertThat(address.getNeighborhood()).isEqualTo("Hood");
        assertThat(address.getStateProvince()).isEqualTo("ST");
        assertThat(address.getAddressLine()).isEqualTo("Apt 1");
        assertThat(address.isMain()).isTrue();
    }

    @Test
    void validatePostalCodeThrowsIfTooShort() {
        assertThrows(InvalidPostalCodeException.class, () -> Address.validatePostalCode("1234567"));
    }

    @Test
    void validatePostalCodeThrowsIfTooLong() {
        assertThrows(InvalidPostalCodeException.class, () -> Address.validatePostalCode("123456789"));
    }

    @Test
    void validatePostalCodeThrowsIfNotAllDigits() {
        assertThrows(InvalidPostalCodeException.class, () -> Address.validatePostalCode("1234567a"));
    }

    @Test
    void patchUpdatesAllFields() {
        Address address = Address.create("Old", 1, "OldCity", "11111111", "OldHood", "OL", "Old", false);
        
        address.patch("New", 2, "NewCity", "22222222", "NewHood", "NW", "New", true);
        
        assertThat(address.getStreetName()).isEqualTo("New");
        assertThat(address.getNumber()).isEqualTo(2);
        assertThat(address.getCity()).isEqualTo("NewCity");
        assertThat(address.getPostalCode()).isEqualTo("22222222");
        assertThat(address.getNeighborhood()).isEqualTo("NewHood");
        assertThat(address.getStateProvince()).isEqualTo("NW");
        assertThat(address.getAddressLine()).isEqualTo("New");
        assertThat(address.isMain()).isTrue();
    }

    @Test
    void patchWithNullsDoesNotUpdate() {
        Address address = Address.create("Street", 123, "City", "12345678", "Hood", "ST", "Apt", true);
        
        address.patch(null, null, null, null, null, null, null, null);
        
        assertThat(address.getStreetName()).isEqualTo("Street");
        assertThat(address.getNumber()).isEqualTo(123);
        assertThat(address.isMain()).isTrue();
    }

    @Test
    void protectedConstructorExists() throws Exception {
        var constructor = Address.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Address address = (Address) constructor.newInstance();
        assertThat(address).isNotNull();
    }
}
