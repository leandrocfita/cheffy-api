package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.presentation.dto.*;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public UserCommandPort toCommand(UserCreateDTO userCreateDTO) {
        AddressCreateDTO address = userCreateDTO.address();

        return new UserCommandPort(
                userCreateDTO.name(),
                userCreateDTO.email(),
                userCreateDTO.login(),
                userCreateDTO.password(),
                new AddressCommandPort(
                        address.streetName(),
                        address.number(),
                        address.city(),
                        address.postalCode(),
                        address.neighborhood(),
                        address.stateProvince(),
                        address.addressLine(),
                        address.main())
        );
    }

    public UserCommandPort toCommand(UserUpdateDTO userUpdateDTO) {
        return new UserCommandPort(
                userUpdateDTO.name(),
                userUpdateDTO.email(),
                userUpdateDTO.login(),
                null,
                null
        );
    }

    public AddressCommandPort toCommand(AddressCreateDTO request) {
        return new AddressCommandPort(
                request.streetName(),
                request.number(),
                request.city(),
                request.postalCode(),
                request.neighborhood(),
                request.stateProvince(),
                request.addressLine(),
                request.main()
        );
    }

    public AddressCommandPort toCommand(AddressPatchDTO request) {
        return new AddressCommandPort(
                request.streetName(),
                request.number(),
                request.city(),
                request.postalCode(),
                request.neighborhood(),
                request.stateProvince(),
                request.addressLine(),
                request.main()
        );
    }

}
