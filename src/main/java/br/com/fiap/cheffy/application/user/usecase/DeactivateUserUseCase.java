package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;
import br.com.fiap.cheffy.domain.user.port.input.DeactivateUserInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;

import java.util.UUID;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class DeactivateUserUseCase implements DeactivateUserInput {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public DeactivateUserUseCase(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void execute(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION, id));

        if (!user.isActive()) {
            throw new UserOperationNotAllowedException(USER_IS_ALREADY_INACTIVE);
        }

        if (restaurantRepository.existsActiveRestaurantByUserId(id)) {
            throw new UserOperationNotAllowedException(USER_HAS_ACTIVE_RESTAURANT);
        }

        user.deactivate();
        userRepository.save(user);
    }
}
