package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.input.RemoveAddressInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;

import java.util.UUID;

public class RemoveAddressUseCase implements RemoveAddressInput {

    private UserServiceHelper userServiceHelper;
    private UserRepository userRepository;

    public RemoveAddressUseCase(
            UserServiceHelper userServiceHelper,
            UserRepository userRepository
    ) {
        this.userServiceHelper = userServiceHelper;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(UUID userId, Long addressId) {

        User user = userServiceHelper.getUserOrFail(userId);

        Address address = user.findAddressByIdOrFail(addressId);

        user.removeAddress(address);

        userRepository.save(user);

    }
}
