package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.input.AddAddressInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;

import java.util.UUID;

public class AddAddressUseCase implements AddAddressInput {

    private final UserRepository userRepository;
    private final UserServiceHelper userServiceHelper;

    public AddAddressUseCase(
            UserRepository userRepository,
            UserServiceHelper userServiceHelper){
        this.userRepository = userRepository;
        this.userServiceHelper = userServiceHelper;
    }


    @Override
    public void execute(AddressCommandPort addressCmPort, UUID userId) {

        User user = userServiceHelper.getUserOrFail(userId);

        Address address = createAddress(addressCmPort);

        user.addAddress(address);

        userRepository.save(user);

    }

    private static Address createAddress(AddressCommandPort addressCmPort) {
        return Address.create(
                addressCmPort.streetName(),
                addressCmPort.number(),
                addressCmPort.city(),
                addressCmPort.postalCode(),
                addressCmPort.neighborhood(),
                addressCmPort.stateProvince(),
                addressCmPort.addressLine(),
                addressCmPort.main()
        );
    }
}
