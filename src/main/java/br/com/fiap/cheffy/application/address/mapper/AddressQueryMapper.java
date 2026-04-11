package br.com.fiap.cheffy.application.address.mapper;

import br.com.fiap.cheffy.application.user.dto.AddressQueryPort;
import br.com.fiap.cheffy.domain.user.entity.Address;

public class AddressQueryMapper {

    public AddressQueryPort toQueryPort(Address address) {
        return new AddressQueryPort(
                address.getId(),
                address.getStreetName(),
                address.getNumber(),
                address.getCity(),
                address.getPostalCode(),
                address.getNeighborhood(),
                address.getStateProvince(),
                address.getAddressLine(),
                address.isMain()
        );
    }
}
