package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.input.UpdateAddressInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;

import java.util.UUID;

public class UpdateAddressUseCase implements UpdateAddressInput {

    private final UserServiceHelper  userServiceHelper;
    private final UserRepository userRepository;

    public UpdateAddressUseCase(
            UserServiceHelper userServiceHelper,
            UserRepository userRepository) {
        this.userServiceHelper = userServiceHelper;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(UUID userId, Long addressId, AddressCommandPort addressCmPort) {

        User user = userServiceHelper.getUserOrFail(userId);

        user.updateAddress(
                addressId,
                addressCmPort.streetName(),
                addressCmPort.number(),
                addressCmPort.city(),
                addressCmPort.postalCode(),
                addressCmPort.neighborhood(),
                addressCmPort.stateProvince(),
                addressCmPort.addressLine(),
                addressCmPort.main());

        userRepository.save(user);

    }

}
