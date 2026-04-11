package br.com.fiap.cheffy.utils;

import br.com.fiap.cheffy.application.user.dto.AddressQueryPort;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.infrastructure.persistence.address.entity.AddressJpaEntity;

import java.time.OffsetDateTime;

public class AddressTestUtils {

    public static AddressQueryPort createAddressQueryPort() {
        return new AddressQueryPort(
                1l,
                "Rua",
                999,
                "Townsville",
                "12345678",
                "Town",
                "TS",
                null,
                true
        );
    }

    public static Address createTestAddressDomainEntity() {
        return new Address(
                1L,
                "Main St",
                123,
                "Test City",
                "12345678",
                "Test Hood",
                "TS",
                null,
                true
        );
    }

    public static AddressJpaEntity createTestAddressJpaEntity() {
        AddressJpaEntity entity = new AddressJpaEntity();
        entity.setId(1L);
        entity.setStreetName("Main St");
        entity.setNumber(123);
        entity.setCity("Test City");
        entity.setPostalCode("12345678");
        entity.setNeighborhood("Test Hood");
        entity.setStateProvince("TS");
        entity.setMain(true);
        entity.setDateCreated(OffsetDateTime.now());
        entity.setLastUpdated(OffsetDateTime.now());
        return entity;
    }
}
