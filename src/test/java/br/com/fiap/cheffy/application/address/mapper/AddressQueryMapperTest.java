package br.com.fiap.cheffy.application.address.mapper;

import br.com.fiap.cheffy.application.user.dto.AddressQueryPort;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.utils.AddressTestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressQueryMapperTest {

    private final AddressQueryMapper mapper = new AddressQueryMapper();

    @Test
    void shouldMapAddressToQueryPort() {
        Address address = AddressTestUtils.createTestAddressDomainEntity();

        AddressQueryPort result = mapper.toQueryPort(address);

        assertThat(result.id()).isEqualTo(address.getId());
        assertThat(result.streetName()).isEqualTo(address.getStreetName());
        assertThat(result.number()).isEqualTo(address.getNumber());
        assertThat(result.city()).isEqualTo(address.getCity());
        assertThat(result.postalCode()).isEqualTo(address.getPostalCode());
        assertThat(result.neighborhood()).isEqualTo(address.getNeighborhood());
        assertThat(result.stateProvince()).isEqualTo(address.getStateProvince());
        assertThat(result.addressLine()).isEqualTo(address.getAddressLine());
        assertThat(result.main()).isEqualTo(address.isMain());
    }
}
