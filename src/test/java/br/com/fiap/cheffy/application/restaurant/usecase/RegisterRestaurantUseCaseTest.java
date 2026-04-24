//package br.com.fiap.cheffy.application.restaurant.usecase;
//
//import br.com.fiap.cheffy.application.restaurant.RestaurantCommandPortTestBuilder;
//import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;
//import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
//import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
//import br.com.fiap.cheffy.domain.profile.ProfileType;
//import br.com.fiap.cheffy.domain.profile.entity.Profile;
//import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
//import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
//import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
//import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
//import br.com.fiap.cheffy.domain.user.entity.User;
//import br.com.fiap.cheffy.shared.exception.InvalidDataException;
//import br.com.fiap.cheffy.shared.exception.RegisterFailedException;
//import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.ZoneId;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class RegisterRestaurantUseCaseTest {
//
//    @Mock
//    private UserServiceHelper userServiceHelper;
//
//    @Mock
//    private RestaurantRepository restaurantRepository;
//
//    @Mock
//    private ProfileRepository profileRepository;
//
//    @Mock
//    private RestaurantServiceHelper restaurantServiceHelper;
//
//    private RegisterRestaurantUseCase useCase;
//
//    @BeforeEach
//    void setUp() {
//        useCase = new RegisterRestaurantUseCase(userServiceHelper, restaurantRepository, profileRepository, restaurantServiceHelper);
//    }
//
//    @Test
//    void executeRegistersRestaurantAndAssignsOwnerProfileWhenMissing() {
//        RestaurantCommandPort command = RestaurantCommandPortTestBuilder.aValidCommand().build();
//
//        UUID userId = UUID.randomUUID();
//        User user = new User(userId, "Owner", "owner@mail.com", "owner", "Password@1234", true);
//        Profile ownerProfile = Profile.create(1L, ProfileType.OWNER.name());
//
//        when(restaurantRepository.existsByName(command.name())).thenReturn(false);
//        when(restaurantRepository.existsByCnpj(command.cnpj())).thenReturn(false);
//        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);
//        when(profileRepository.findByType(ProfileType.OWNER.name())).thenReturn(Optional.of(ownerProfile));
//        when(restaurantServiceHelper.extractZoneId(command.zoneId())).thenReturn(ZoneId.of(command.zoneId()));
//
//        UUID savedRestaurantId = UUID.randomUUID();
//        Restaurant savedRestaurant = mock(Restaurant.class);
//        when(savedRestaurant.getId()).thenReturn(savedRestaurantId);
//        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);
//
//        String result = useCase.execute(command, userId);
//
//        ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);
//        verify(restaurantRepository).save(restaurantCaptor.capture());
//        Restaurant restaurantToSave = restaurantCaptor.getValue();
//
//        assertThat(result).isEqualTo(savedRestaurant.getId().toString());
//        assertThat(restaurantToSave.getAddress()).isNotNull();
//        assertThat(restaurantToSave.getAddress().getStreetName()).isEqualTo(command.address().streetName());
//        assertThat(user.getProfiles()).contains(ownerProfile);
//        verify(userServiceHelper).saveUser(user);
//    }
//
//    @Test
//    void executeDoesNotAssignOwnerProfileWhenUserAlreadyHasOwner() {
//        RestaurantCommandPort command = RestaurantCommandPortTestBuilder.aValidCommand().build();
//
//        UUID userId = UUID.randomUUID();
//        UUID savedRestaurantId = UUID.randomUUID();
//        User user = new User(userId, "Owner", "owner@mail.com", "owner", "Password@1234", true);
//        Profile ownerProfile = Profile.create(1L, ProfileType.OWNER.name());
//        user.addProfile(ownerProfile);
//
//        when(restaurantRepository.existsByName(command.name())).thenReturn(false);
//        when(restaurantRepository.existsByCnpj(command.cnpj())).thenReturn(false);
//        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);
//        when(restaurantServiceHelper.extractZoneId(command.zoneId())).thenReturn(ZoneId.of(command.zoneId()));
//
//        Restaurant savedRestaurant = mock(Restaurant.class);
//        when(savedRestaurant.getId()).thenReturn(savedRestaurantId);
//        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);
//
//        String result = useCase.execute(command, userId);
//
//        assertThat(result).isEqualTo(savedRestaurantId.toString());
//        verify(profileRepository, never()).findByType(any());
//        verify(userServiceHelper, never()).saveUser(any(User.class));
//    }
//
//
//
//    @Test
//    void executeThrowsWhenRestaurantAlreadyExistsByName() {
//        RestaurantCommandPort command = RestaurantCommandPortTestBuilder.aValidCommand().build();
//
//        when(restaurantRepository.existsByName(command.name())).thenReturn(true);
//
//        assertThrows(RegisterFailedException.class, () -> useCase.execute(command, UUID.randomUUID()));
//
//        verify(userServiceHelper, never()).getUserOrFail(any());
//        verify(restaurantRepository, never()).save(any());
//    }
//
//    @Test
//    void executeThrowsWhenRestaurantAlreadyExistsByCnpj() {
//        RestaurantCommandPort command = RestaurantCommandPortTestBuilder.aValidCommand().build();
//
//        when(restaurantRepository.existsByCnpj(command.cnpj())).thenReturn(true);
//
//        assertThrows(RegisterFailedException.class, () -> useCase.execute(command, UUID.randomUUID()));
//
//        verify(userServiceHelper, never()).getUserOrFail(any());
//        verify(restaurantRepository, never()).save(any());
//    }
//
//    @Test
//    void executeThrowsWhenOwnerProfileIsMissing() {
//        RestaurantCommandPort command = RestaurantCommandPortTestBuilder.aValidCommand().build();
//
//        UUID userId = UUID.randomUUID();
//        User user = new User(userId, "Owner", "owner@mail.com", "owner", "Password@1234", true);
//
//        when(restaurantRepository.existsByName(command.name())).thenReturn(false);
//        when(restaurantRepository.existsByCnpj(command.cnpj())).thenReturn(false);
//        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);
//        when(restaurantServiceHelper.extractZoneId(command.zoneId())).thenReturn(ZoneId.of(command.zoneId()));
//
//        UUID savedRestaurantId = UUID.randomUUID();
//        Restaurant savedRestaurant = mock(Restaurant.class);
//        when(savedRestaurant.getId()).thenReturn(savedRestaurantId);
//        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);
//
//        when(profileRepository.findByType(ProfileType.OWNER.name())).thenReturn(Optional.empty());
//
//        assertThrows(ProfileNotFoundException.class, () -> useCase.execute(command, userId));
//
//        verify(userServiceHelper, never()).saveUser(any(User.class));
//    }
//
//    @Test
//    void executeThrowsWhenZoneIdIsInvalid() {
//        RestaurantCommandPort command = RestaurantCommandPortTestBuilder.aValidCommand()
//                .withZoneId("Invalid")
//                .build();
//        UUID userId = UUID.randomUUID();
//        User user = new User(userId, "Owner", "owner@mail.com", "owner", "Password@1234", true);
//
//        when(restaurantRepository.existsByName(command.name())).thenReturn(false);
//        when(restaurantRepository.existsByCnpj(command.cnpj())).thenReturn(false);
//        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);
//        when(restaurantServiceHelper.extractZoneId("Invalid")).thenThrow(new InvalidDataException(ExceptionsKeys.ZONE_ID_DO_NOT_EXIST));
//
//        assertThrows(InvalidDataException.class, () -> useCase.execute(command, userId));
//
//        verify(userServiceHelper, never()).saveUser(any(User.class));
//    }
//
//    @Test
//    void executeRegistersOpen24hRestaurant() {
//        RestaurantCommandPort command = new RestaurantCommandPort(
//                "Restaurante 24h", "Italiana", "27865757000102",
//                null, null, "America/Sao_Paulo", true,
//                new br.com.fiap.cheffy.application.user.dto.AddressCommandPort(
//                        "Rua A", 123, "Sao Paulo", "01001000", "Centro", "SP", null, null));
//        UUID userId = UUID.randomUUID();
//        User user = new User(userId, "Owner", "owner@mail.com", "owner", "Password@1234", true);
//        Profile ownerProfile = Profile.create(1L, ProfileType.OWNER.name());
//
//        when(restaurantRepository.existsByName(command.name())).thenReturn(false);
//        when(restaurantRepository.existsByCnpj(command.cnpj())).thenReturn(false);
//        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);
//        when(profileRepository.findByType(ProfileType.OWNER.name())).thenReturn(Optional.of(ownerProfile));
//        when(restaurantServiceHelper.extractZoneId(command.zoneId())).thenReturn(ZoneId.of(command.zoneId()));
//
//        Restaurant savedRestaurant = mock(Restaurant.class);
//        when(savedRestaurant.getId()).thenReturn(UUID.randomUUID());
//        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(savedRestaurant);
//
//        String result = useCase.execute(command, userId);
//
//        assertThat(result).isNotNull();
//    }
//}
