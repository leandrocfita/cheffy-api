package br.com.fiap.cheffy.infrastructure.persistence.address.mapper;

import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.infrastructure.persistence.address.entity.AddressJpaEntity;
import br.com.fiap.cheffy.infrastructure.persistence.user.entity.UserJpaEntity;
import br.com.fiap.cheffy.utils.AddressTestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressPersistenceMapperTest {

    private final AddressPersistenceMapper addressPersistenceMapper = new AddressPersistenceMapper();

    @Test
    void toAddressJpaEntity() {
        Address address = AddressTestUtils.createTestAddressDomainEntity();

        AddressJpaEntity result = addressPersistenceMapper.toJpa(address);

        assertNotNull(result);
        assertEquals(address.getId(), result.getId());
        assertEquals(address.getStreetName(), result.getStreetName());
    }

    @Test
    void toAddressDomain() {
        AddressJpaEntity address = AddressTestUtils.createTestAddressJpaEntity();

        Address result = addressPersistenceMapper.toDomain(address);

        assertNotNull(result);
        assertEquals(address.getId(), result.getId());
        assertEquals(address.getStreetName(), result.getStreetName());
    }

}