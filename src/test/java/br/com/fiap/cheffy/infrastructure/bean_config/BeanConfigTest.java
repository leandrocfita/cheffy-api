//package br.com.fiap.cheffy.infrastructure.bean_config;
//
//import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
//import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
//import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
//import br.com.fiap.cheffy.application.user.usecase.*;
//import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
//import br.com.fiap.cheffy.domain.user.port.output.AuthUserExternalClient;
//import br.com.fiap.cheffy.infrastructure.bean_config.ProfileUseCaseConfig;
//import br.com.fiap.cheffy.domain.user.port.input.AuthenticationManagerPort;
//import br.com.fiap.cheffy.domain.user.port.input.PasswordEncoderPort;
//import br.com.fiap.cheffy.domain.user.port.input.TokenGeneratorPort;
//import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(MockitoExtension.class)
//class BeanConfigTest {
//
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private ProfileRepository profileRepository;
//    @Mock
//    private PasswordEncoderPort passwordEncoder;
//    @Mock
//    private AuthenticationManagerPort authManager;
//    @Mock
//    private TokenGeneratorPort tokenGenerator;
//    @Mock
//    private UserQueryMapper userQueryMapper;
//    @Mock
//    private RestaurantRepository restaurantRepository;
//    @Mock
//    private AuthUserExternalClient authClient;
//
//    @Test
//    void userUseCaseConfigCreatesBeans() {
//        UserUseCaseConfig config = new UserUseCaseConfig();
//
//        UserServiceHelper helper = config.userServiceHelper(userRepository, userQueryMapper);
//        assertThat(helper).isNotNull();
//
//        DeactivateUserUseCase deactivate = config.deactivateUserUseCase(userRepository, restaurantRepository);
//        assertThat(deactivate).isNotNull();
//
//        CreateUserUseCase createUser = config.createUserUseCase(userRepository, profileRepository, authClient);
//        assertThat(createUser).isNotNull();
//
//        UpdateUserUseCase updateUser = config.updateUserUseCase(userRepository);
//        assertThat(updateUser).isNotNull();
//
//        AddAddressUseCase addAddress = config.addAddressUseCase(userRepository, helper);
//        assertThat(addAddress).isNotNull();
//
//        UpdateAddressUseCase updateAddress = config.updateAddressUseCase(userRepository, helper);
//        assertThat(updateAddress).isNotNull();
//
//        RemoveAddressUseCase removeAddress = config.removeAddressUseCase(helper, userRepository);
//        assertThat(removeAddress).isNotNull();
//
//        LoginUseCase login = config.loginUseCase(authManager, tokenGenerator);
//        assertThat(login).isNotNull();
//
//
//        ListAllUsersUseCase listAll = config.listAllUsersUseCase(userRepository, userQueryMapper);
//        assertThat(listAll).isNotNull();
//
//        FindUserByIdUseCase findById = config.findUserByIdUseCase(userRepository, userQueryMapper);
//        assertThat(findById).isNotNull();
//
//        ReactivateUserUseCase reactivate = config.reactivateUserUseCase(userRepository);
//        assertThat(reactivate).isNotNull();
//    }
//
//    @Test
//    void profileUseCaseConfigCreatesBeans() {
//        ProfileUseCaseConfig config = new ProfileUseCaseConfig();
//        assertThat(config.createProfileUseCase(profileRepository)).isNotNull();
//    }
//}
