package br.com.fiap.cheffy.infrastructure.persistence.address.mapper;

import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.infrastructure.persistence.address.entity.AddressJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AddressPersistenceMapper {

    public AddressPersistenceMapper() {
    }


    public AddressJpaEntity toJpa(Address address) {
        AddressJpaEntity jpa = new AddressJpaEntity();

        jpa.setId(address.getId());
        jpa.setStreetName(address.getStreetName());
        jpa.setNumber(address.getNumber());
        jpa.setCity(address.getCity());
        jpa.setPostalCode(address.getPostalCode());
        jpa.setNeighborhood(address.getNeighborhood());
        jpa.setStateProvince(address.getStateProvince());
        jpa.setAddressLine(address.getAddressLine());
        jpa.setMain(address.isMain());

        return jpa;
    }

    public Address toDomain(AddressJpaEntity jpa) {
        return new Address(
                jpa.getId(),
                jpa.getStreetName(),
                jpa.getNumber(),
                jpa.getCity(),
                jpa.getPostalCode(),
                jpa.getNeighborhood(),
                jpa.getStateProvince(),
                jpa.getAddressLine(),
                jpa.getMain()
        );
    }
}
