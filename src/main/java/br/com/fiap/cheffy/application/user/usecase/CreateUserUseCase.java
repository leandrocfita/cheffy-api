package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.AuthUserCommandPort;
import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.AuthStatus;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.input.CreateUserInput;
import br.com.fiap.cheffy.domain.user.port.output.AuthUserExternalClient;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import br.com.fiap.cheffy.infrastructure.exception.LoginAlreadyExistsException;
import br.com.fiap.cheffy.shared.exception.RegisterFailedException;

import static br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys.*;

public class CreateUserUseCase implements CreateUserInput {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final AuthUserExternalClient authClient;

    public CreateUserUseCase(
            UserRepository userRepository,
            ProfileRepository profileRepository,
            AuthUserExternalClient authClient
    ) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.authClient = authClient;
    }

    public String execute(UserCommandPort command){

        User user = userRepository.findByEmail(command.email())
                .map(existingUser -> {

                    failCaseUserAlreadyExists(existingUser);

                    return updatePendingUser(command, existingUser);

                }).orElseGet(()-> createNewUser(command));


        return processAuthIntegration(command, user);

    }

    private User createNewUser(UserCommandPort command) {
        User newUser = createUserDomain(command);
        Address address = createAddressDomain(command);
        newUser.addAddress(address);

        return userRepository.save(newUser);
    }

    private User updatePendingUser(UserCommandPort command, User existingUser) {
        existingUser.updatePendingUserInformation(command.name(), createAddressDomain(command));
        return userRepository.save(existingUser);
    }

    private static void failCaseUserAlreadyExists(User existingUser) {
        if(existingUser.getAuthStatus() == AuthStatus.CONFIRMED){
            throw new RegisterFailedException(REGISTER_FAILED_EXCEPTION);
        }
    }

    private String processAuthIntegration(UserCommandPort command, User user)  {
        String userIdString = user.getId().toString();

        try {
            String authId = createAuthUserOrFail(command, userIdString);
            user.finishAuthIntegration(authId);
            userRepository.save(user);
        } catch (Exception e){
            throw e;
        }
        return userIdString;
    }


    private String createAuthUserOrFail(UserCommandPort command, String userId) {
        String authId = null;
        try {
            authId = authClient.createUser(
                    new AuthUserCommandPort(
                        command.login(),
                        command.password(),
                        userId
                    )
            );
        } catch (LoginAlreadyExistsException e) {
            throw new RegisterFailedException(REGISTER_FAILED_EXCEPTION);
        }
        return authId;
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
                findClientProfile()
        );
    }

    private Profile findClientProfile() {
        String profileType = ProfileType.CLIENT.name();

        return profileRepository.findByType(profileType)
                .orElseThrow(() -> new ProfileNotFoundException(PROFILE_NOT_FOUND_EXCEPTION,
                        profileType));
    }



}
