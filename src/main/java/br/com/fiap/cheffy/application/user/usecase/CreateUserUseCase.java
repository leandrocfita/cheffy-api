package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.user.port.input.PasswordEncoderPort;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.input.CreateUserInput;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import br.com.fiap.cheffy.shared.exception.RegisterFailedException;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class CreateUserUseCase implements CreateUserInput {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoderPort passwordEncoder;

    public CreateUserUseCase(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            PasswordEncoderPort passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String execute(UserCommandPort command){
        User user = createUserDomain(command);

        checkLoginAndEmailAvailability(user);

        Address address = createAddressDomain(command);
        user.addAddress(address);

        return userRepository.save(user).getId().toString();

    }

    private Address createAddressDomain(UserCommandPort command) {

        if(command.address().main().equals(Boolean.FALSE)){
            throw new RegisterFailedException(FIRST_ADDRESS_MUST_BE_MAIN);
        }

               return Address.create(
                        command.address().streetName(),
                        command.address().number(),
                        command.address().city(),
                        command.address().postalCode(),
                        command.address().neighborhood(),
                        command.address().stateProvince(),
                        command.address().addressLine(),
                        command.address().main()
                );
    }

    private User createUserDomain(UserCommandPort command) {
        return User.create(
                command.name(),
                command.email(),
                command.login(),
                processPassword(command.password()),
                findClientProfile()
        );
    }

    private Profile findClientProfile() {
        String profileType = ProfileType.CLIENT.name();

        return profileRepository.findByType(profileType)
                .orElseThrow(() -> new ProfileNotFoundException(PROFILE_NOT_FOUND_EXCEPTION,
                        profileType));
    }

    private String processPassword(String rawPassword) {
        User.validatePassword(rawPassword);

        return passwordEncoder.encode(rawPassword);
    }

    private void checkLoginAndEmailAvailability(User user) {
        if (userRepository.existsByEmailOrLogin(user.getEmail(), user.getLogin())) {
            throw new RegisterFailedException(REGISTER_FAILED_EXCEPTION);
        }
    }
}
