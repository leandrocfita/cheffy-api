//package br.com.fiap.cheffy.application.user.usecase;
//
//import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
//import br.com.fiap.cheffy.domain.user.entity.User;
//import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
//import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
//import br.com.fiap.cheffy.shared.exception.InvalidOperationException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UpdateUserUseCaseTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UpdateUserUseCase updateUserUseCase;
//
//    private UUID userId;
//    private User existingUser;
//
//    @BeforeEach
//    void setUp() {
//        userId = UUID.randomUUID();
//        existingUser = new User(userId, "John Doe", "john@email.com", "john.doe", "encodedPass", true);
//    }
//
//    @Test
//    void shouldUpdateUserNameSuccessfully() {
//        UserCommandPort command = new UserCommandPort("Jane Doe", null, null, null, null);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//        updateUserUseCase.execute(userId, command);
//
//        verify(userRepository).findById(userId);
//        verify(userRepository).save(existingUser);
//        assertEquals("Jane Doe", existingUser.getName());
//    }
//
//    @Test
//    void shouldUpdateUserEmailSuccessfully() {
//        UserCommandPort command = new UserCommandPort(null, "newemail@email.com", null, null, null);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.findByEmail("newemail@email.com")).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//        updateUserUseCase.execute(userId, command);
//
//        verify(userRepository).findByEmail("newemail@email.com");
//        verify(userRepository).save(existingUser);
//        assertEquals("newemail@email.com", existingUser.getEmail());
//    }
//
//    @Test
//    void shouldUpdateUserLoginSuccessfully() {
//        UserCommandPort command = new UserCommandPort(null, null, "newlogin", null, null);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.findByLogin("newlogin")).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//        updateUserUseCase.execute(userId, command);
//
//        verify(userRepository).findByLogin("newlogin");
//        verify(userRepository).save(existingUser);
//        assertEquals("newlogin", existingUser.getLogin());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUserNotFound() {
//        UserCommandPort command = new UserCommandPort("Jane Doe", null, null, null, null);
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> updateUserUseCase.execute(userId, command));
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldThrowInvalidOperationExceptionWhenEmailAlreadyExists() {
//        UUID otherUserId = UUID.randomUUID();
//        User otherUser = new User(otherUserId, "Other", "new@email.com", "other", "pass", true);
//        UserCommandPort command = new UserCommandPort(null, "new@email.com", null, null, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.of(otherUser));
//
//        assertThrows(InvalidOperationException.class, () -> updateUserUseCase.execute(userId, command));
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldThrowInvalidOperationExceptionWhenLoginAlreadyExists() {
//        UUID otherUserId = UUID.randomUUID();
//        User otherUser = new User(otherUserId, "Other", "other@email.com", "newlogin", "pass", true);
//        UserCommandPort command = new UserCommandPort(null, null, "newlogin", null, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.findByLogin("newlogin")).thenReturn(Optional.of(otherUser));
//
//        assertThrows(InvalidOperationException.class, () -> updateUserUseCase.execute(userId, command));
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldAllowUpdateWithSameUserEmailAndLogin() {
//        UserCommandPort command = new UserCommandPort(null, "john@email.com", "john.doe", null, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.of(existingUser));
//        when(userRepository.findByLogin("john.doe")).thenReturn(Optional.of(existingUser));
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//        assertDoesNotThrow(() -> updateUserUseCase.execute(userId, command));
//        verify(userRepository).save(existingUser);
//    }
//
//    @Test
//    void shouldNotValidateEmailWhenNull() {
//        UserCommandPort command = new UserCommandPort("New Name", null, null, null, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//        updateUserUseCase.execute(userId, command);
//
//        verify(userRepository, never()).findByEmail(any());
//        verify(userRepository).save(existingUser);
//    }
//
//    @Test
//    void shouldNotValidateLoginWhenNull() {
//        UserCommandPort command = new UserCommandPort("New Name", null, null, null, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//        updateUserUseCase.execute(userId, command);
//
//        verify(userRepository, never()).findByLogin(any());
//        verify(userRepository).save(existingUser);
//    }
//
//    @Test
//    void shouldUpdateAllFieldsAtOnce() {
//        UserCommandPort command = new UserCommandPort("New Name", "new@email.com", "newlogin", null, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(userRepository.findByEmail("new@email.com")).thenReturn(Optional.empty());
//        when(userRepository.findByLogin("newlogin")).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//        updateUserUseCase.execute(userId, command);
//
//        verify(userRepository).findByEmail("new@email.com");
//        verify(userRepository).findByLogin("newlogin");
//        verify(userRepository).save(existingUser);
//        assertEquals("New Name", existingUser.getName());
//        assertEquals("new@email.com", existingUser.getEmail());
//        assertEquals("newlogin", existingUser.getLogin());
//    }
//}
