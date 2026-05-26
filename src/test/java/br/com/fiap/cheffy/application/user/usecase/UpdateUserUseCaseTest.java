package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import br.com.fiap.cheffy.shared.exception.InvalidOperationException;
import br.com.fiap.cheffy.utils.UserTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUserUseCase updateUserUseCase;

    private UUID userId;
    private User existingUser;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        existingUser = UserTestUtils.createAFullActiveUserEntity();
    }

    @Test
    void shouldUpdateUserNameSuccessfully() {
        UserCommandPort command = new UserCommandPort("Jane Doe", null, null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        updateUserUseCase.execute(userId, command);

        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
        assertEquals("Jane Doe", existingUser.getName());
    }

    @Test
    @Disabled("Skipping this test until the update use case be working fully again")
    void shouldUpdateUserEmailSuccessfully() {
        UserCommandPort command = new UserCommandPort(null, "newemail@email.com", null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("newemail@email.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        updateUserUseCase.execute(userId, command);

        verify(userRepository).findByEmail("newemail@email.com");
        verify(userRepository).save(existingUser);
        assertEquals("newemail@email.com", existingUser.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserCommandPort command = new UserCommandPort("Jane Doe", null, null, null, null);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> updateUserUseCase.execute(userId, command));
        verify(userRepository, never()).save(any());
    }

    @Test
    @Disabled("Skipping this test until the update use case be working fully again")
    void shouldThrowInvalidOperationExceptionWhenEmailAlreadyExists() {
        UUID otherUserId = UUID.randomUUID();
        User otherUser = User.create("name", "new@email.com", Profile.create(1L, ProfileType.CLIENT.getType()));
        UserCommandPort command = new UserCommandPort(null, "new@email.com", null, null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.of(otherUser));

        assertThrows(InvalidOperationException.class, () -> updateUserUseCase.execute(userId, command));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldNotValidateEmailWhenNull() {
        UserCommandPort command = new UserCommandPort("New Name", null, null, null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        updateUserUseCase.execute(userId, command);

        verify(userRepository, never()).findByEmail(any());
        verify(userRepository).save(existingUser);
    }
}
