//package br.com.fiap.cheffy.application.user.usecase;
//
//import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
//import br.com.fiap.cheffy.domain.user.entity.User;
//import br.com.fiap.cheffy.domain.user.exception.InvalidPasswordException;
//import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
//import br.com.fiap.cheffy.domain.user.port.input.PasswordEncoderPort;
//import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
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
//class UpdateUserPasswordUseCaseTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoderPort passwordEncoderPort;
//
//    @InjectMocks
//    private UpdateUserPasswordUseCase updateUserPasswordUseCase;
//
//    private UUID userId;
//    private User existingUser;
//
//    @BeforeEach
//    void setUp() {
//        userId = UUID.randomUUID();
//        existingUser = new User(userId, "John Doe", "john@email.com", "john.doe", "encodedOldPass", true);
//    }
//
//    @Test
//    void shouldUpdatePasswordSuccessfully() {
//        String rawPassword = "NewPassword@123";
//        String encodedPassword = "encodedNewPassword";
//        UserCommandPort command = new UserCommandPort(null, null, null, rawPassword, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(passwordEncoderPort.encode(rawPassword)).thenReturn(encodedPassword);
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//        updateUserPasswordUseCase.execute(command, userId);
//
//        verify(userRepository).findById(userId);
//        verify(passwordEncoderPort).encode(rawPassword);
//        verify(userRepository).save(existingUser);
//        assertEquals(encodedPassword, existingUser.getPassword());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUserNotFound() {
//        String rawPassword = "NewPassword@123";
//        UserCommandPort command = new UserCommandPort(null, null, null, rawPassword, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> updateUserPasswordUseCase.execute(command, userId));
//        verify(userRepository).findById(userId);
//        verify(passwordEncoderPort, never()).encode(any());
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPasswordIsTooShort() {
//        String rawPassword = "Short@1";
//        UserCommandPort command = new UserCommandPort(null, null, null, rawPassword, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(passwordEncoderPort.encode(rawPassword)).thenReturn("encoded");
//
//        assertThrows(InvalidPasswordException.class, () -> updateUserPasswordUseCase.execute(command, userId));
//        verify(userRepository).findById(userId);
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPasswordHasNoUppercase() {
//        String rawPassword = "nouppercas@123";
//        UserCommandPort command = new UserCommandPort(null, null, null, rawPassword, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(passwordEncoderPort.encode(rawPassword)).thenReturn("encoded");
//
//        assertThrows(InvalidPasswordException.class, () -> updateUserPasswordUseCase.execute(command, userId));
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPasswordHasNoLowercase() {
//        String rawPassword = "NOLOWERCASE@123";
//        UserCommandPort command = new UserCommandPort(null, null, null, rawPassword, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(passwordEncoderPort.encode(rawPassword)).thenReturn("encoded");
//
//        assertThrows(InvalidPasswordException.class, () -> updateUserPasswordUseCase.execute(command, userId));
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPasswordHasNoDigit() {
//        String rawPassword = "NoDigitPass@word";
//        UserCommandPort command = new UserCommandPort(null, null, null, rawPassword, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(passwordEncoderPort.encode(rawPassword)).thenReturn("encoded");
//
//        assertThrows(InvalidPasswordException.class, () -> updateUserPasswordUseCase.execute(command, userId));
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPasswordHasNoSymbol() {
//        String rawPassword = "NoSymbolPass123";
//        UserCommandPort command = new UserCommandPort(null, null, null, rawPassword, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(passwordEncoderPort.encode(rawPassword)).thenReturn("encoded");
//
//        assertThrows(InvalidPasswordException.class, () -> updateUserPasswordUseCase.execute(command, userId));
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPasswordIsNull() {
//        UserCommandPort command = new UserCommandPort(null, null, null, null, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(passwordEncoderPort.encode(null)).thenReturn("encoded");
//
//        assertThrows(InvalidPasswordException.class, () -> updateUserPasswordUseCase.execute(command, userId));
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void shouldEncodePasswordBeforeValidation() {
//        String rawPassword = "ValidPassword@123";
//        String encodedPassword = "encodedValidPassword";
//        UserCommandPort command = new UserCommandPort(null, null, null, rawPassword, null);
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
//        when(passwordEncoderPort.encode(rawPassword)).thenReturn(encodedPassword);
//        when(userRepository.save(any(User.class))).thenReturn(existingUser);
//
//        updateUserPasswordUseCase.execute(command, userId);
//
//        verify(passwordEncoderPort).encode(rawPassword);
//        assertEquals(encodedPassword, existingUser.getPassword());
//    }
//}
