package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.profile.exception.ProfileNotFoundException;
import br.com.fiap.cheffy.domain.profile.port.output.ProfileRepository;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.input.PasswordEncoderPort;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import br.com.fiap.cheffy.shared.exception.RegisterFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCase(userRepository, profileRepository, passwordEncoder);
    }

    @Test
    void executeCreatesUserWithAddressAndEncodesPassword() {
        UserCommandPort command = buildCommand();
        Profile profile = Profile.create(7L, ProfileType.CLIENT.name());
        String encodedPassword = "encoded-password";
        User savedUser = new User(UUID.randomUUID(), command.name(), command.email(), command.login(), encodedPassword, true);

        when(profileRepository.findByType(ProfileType.CLIENT.name())).thenReturn(Optional.of(profile));
        when(passwordEncoder.encode(command.password())).thenReturn(encodedPassword);
        when(userRepository.existsByEmailOrLogin(command.email(), command.login())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        String result = createUserUseCase.execute(command);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals(savedUser.getId().toString(), result);
        assertEquals(encodedPassword, capturedUser.getPassword());

        Set<Address> addresses = capturedUser.getAddresses();
        assertEquals(1, addresses.size());
        Address address = addresses.iterator().next();
        assertEquals(command.address().streetName(), address.getStreetName());
        assertEquals(command.address().number(), address.getNumber());
        assertEquals(command.address().city(), address.getCity());
        assertEquals(command.address().postalCode(), address.getPostalCode());
        assertEquals(command.address().neighborhood(), address.getNeighborhood());
        assertEquals(command.address().stateProvince(), address.getStateProvince());
        assertEquals(command.address().addressLine(), address.getAddressLine());
        assertTrue(address.isMain());
        assertNotNull(address.getUser());
    }

    @Test
    void executeThrowsWhenProfileIsMissing() {
        UserCommandPort command = buildCommand();

        when(profileRepository.findByType(ProfileType.CLIENT.name())).thenReturn(Optional.empty());

        assertThrows(ProfileNotFoundException.class, () -> createUserUseCase.execute(command));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void executeThrowsWhenEmailOrLoginAlreadyExists() {
        UserCommandPort command = buildCommand();
        Profile profile = Profile.create(7L, ProfileType.CLIENT.name());
        when(profileRepository.findByType(ProfileType.CLIENT.name())).thenReturn(Optional.of(profile));
        when(passwordEncoder.encode(command.password())).thenReturn("encoded-password");
        when(userRepository.existsByEmailOrLogin(command.email(), command.login())).thenReturn(true);

        assertThrows(RegisterFailedException.class, () -> createUserUseCase.execute(command));

        verify(userRepository, never()).save(any(User.class));
    }

    private UserCommandPort buildCommand() {
        AddressCommandPort address = new AddressCommandPort(
                "Rua A",
                123,
                "Sao Paulo",
                "01000000",
                "Centro",
                "SP",
                "Ap 11",
                true
        );

        return new UserCommandPort(
                "Jane Doe",
                "jane.doe@example.com",
                "jane.doe",
                "ValidPass1!XX",
                address
        );
    }
}
