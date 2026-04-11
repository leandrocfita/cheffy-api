package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.application.user.usecase.*;
import br.com.fiap.cheffy.application.user.usecase.AddAddressUseCase;
import br.com.fiap.cheffy.application.user.usecase.CreateUserUseCase;
import br.com.fiap.cheffy.application.user.usecase.LoginUseCase;
import br.com.fiap.cheffy.application.user.usecase.UpdateAddressUseCase;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.domain.user.port.input.AuthenticationManagerPort;
import br.com.fiap.cheffy.domain.user.port.input.PasswordEncoderPort;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.user.port.input.TokenGeneratorPort;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public ReactivateUserUseCase reactivateUserUseCase(
            UserRepository userRepository
    ) {
        return new ReactivateUserUseCase(userRepository);
    }

    @Bean
    public DeactivateUserUseCase deactivateUserUseCase (
            UserRepository userRepository,
            RestaurantRepository restaurantRepository
    ) {
        return new DeactivateUserUseCase(
                userRepository,
                restaurantRepository
        );
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(
            UserRepository userRepository
    ) {
        return new UpdateUserUseCase(
                userRepository
        );
    }

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            PasswordEncoderPort passwordEncoderPort
    ) {
        return new CreateUserUseCase(
                userRepository,
                profileRepository,
                passwordEncoderPort);
    }

    @Bean
    public UpdateUserPasswordUseCase updateUserPasswordUseCase(
            UserRepository userRepository,
            PasswordEncoderPort passwordEncoderPort
    ) {
        return new UpdateUserPasswordUseCase(
                userRepository,
                passwordEncoderPort
        );
    }

    @Bean
    public UserServiceHelper userServiceHelper(
            UserRepository userRepository,
            UserQueryMapper mapper
    ) {
        return new UserServiceHelper(
                userRepository,
                mapper);

    }

    @Bean
    public LoginUseCase loginUseCase(
            AuthenticationManagerPort authenticationManagerPort,
            TokenGeneratorPort tokenGeneratorPort
    ){
        return new LoginUseCase(
                authenticationManagerPort,
                tokenGeneratorPort
        );
    }

    @Bean
    public AddAddressUseCase addAddressUseCase(
            UserRepository userRepository,
            UserServiceHelper userServiceHelper
    ) {
        return new AddAddressUseCase(
                userRepository,
                userServiceHelper
        );
    }

    @Bean
    public UpdateAddressUseCase updateAddressUseCase(
            UserRepository userRepository,
            UserServiceHelper userServiceHelper
    ) {
        return new UpdateAddressUseCase(
                userServiceHelper,
                userRepository
        );
    }

    @Bean
    public RemoveAddressUseCase removeAddressUseCase(
            UserServiceHelper userServiceHelper,
            UserRepository userRepository
    ) {
        return new RemoveAddressUseCase(
                userServiceHelper,
                userRepository
        );
    }

    @Bean
    public ListAllUsersUseCase listAllUsersUseCase(
            UserRepository userRepository,
            UserQueryMapper mapper
    ) {
        return new ListAllUsersUseCase(
                userRepository,
                mapper
        );
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(
            UserRepository userRepository,
            UserQueryMapper mapper
    ) {
        return new FindUserByIdUseCase(
                userRepository,
                mapper
        );
    }

    @Bean
    public FindUserByNameUseCase findUserByNameUseCase(
            UserRepository userRepository,
            UserQueryMapper mapper
    ) {
        return new FindUserByNameUseCase(
                userRepository,
                mapper
        );
    }
}
