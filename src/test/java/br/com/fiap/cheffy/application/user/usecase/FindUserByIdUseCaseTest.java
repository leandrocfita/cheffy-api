package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserQueryMapper mapper;

    private FindUserByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindUserByIdUseCase(userRepository, mapper);
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        UUID userId = UUID.randomUUID();
        User user = createUser("João Silva", "joao.silva@email.com");
        UserQueryPort queryPort = mock(UserQueryPort.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.toQuery(user)).thenReturn(queryPort);

        UserQueryPort result = useCase.execute(userId);

        assertNotNull(result);
        assertEquals(queryPort, result);
        verify(userRepository, times(1)).findById(userId);
        verify(mapper, times(1)).toQuery(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            useCase.execute(userId);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(mapper, never()).toQuery(any());
    }

    @Test
    void shouldMapUserWithAllRelations() {
        UUID userId = UUID.randomUUID();
        User user = createUser("Maria Oliveira", "maria.oliveira@email.com");
        UserQueryPort queryPort = mock(UserQueryPort.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.toQuery(user)).thenReturn(queryPort);

        UserQueryPort result = useCase.execute(userId);

        assertNotNull(result);
        verify(mapper).toQuery(argThat(u ->
                u.getName().equals("Maria Oliveira") &&
                        u.getEmail().equals("maria.oliveira@email.com") &&
                        !u.getProfiles().isEmpty() &&
                        !u.getAddresses().isEmpty()
        ));
    }

    private User createUser(String name, String email) {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());
        User user = User.create(
                name,
                email,
                name.toLowerCase().replace(" ", "."),
                "Senha@123456",
                profile
        );

        Address address = Address.create(
                "Avenida Paulista",
                1000,
                "São Paulo",
                "01310100",
                "Bela Vista",
                "SP",
                "Apto 101",
                true
        );

        user.addAddress(address);

        return user;
    }
}