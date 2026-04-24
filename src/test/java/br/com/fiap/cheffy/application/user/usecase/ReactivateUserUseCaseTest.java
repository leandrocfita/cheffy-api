//package br.com.fiap.cheffy.application.user.usecase;
//
//import br.com.fiap.cheffy.domain.user.entity.User;
//import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
//import br.com.fiap.cheffy.domain.user.exception.UserOperationNotAllowedException;
//import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ReactivateUserUseCaseTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    private ReactivateUserUseCase useCase;
//
//    @BeforeEach
//    void setUp() {
//        useCase = new ReactivateUserUseCase(userRepository);
//    }
//
//    @Test
//    void executeReactivatesInactiveUser() {
//        UUID id = UUID.randomUUID();
//        User user = new User(id, "Name", "email@test.com", "login", "pass", false);
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//
//        useCase.execute(id);
//
//        verify(userRepository).save(user);
//        assert user.isActive();
//    }
//
//    @Test
//    void executeThrowsWhenUserNotFound() {
//        UUID id = UUID.randomUUID();
//        when(userRepository.findById(id)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> useCase.execute(id));
//        verify(userRepository, never()).save(any());
//    }
//
//    @Test
//    void executeThrowsWhenUserAlreadyActive() {
//        UUID id = UUID.randomUUID();
//        User user = new User(id, "Name", "email@test.com", "login", "pass", true);
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//
//        assertThrows(UserOperationNotAllowedException.class, () -> useCase.execute(id));
//        verify(userRepository, never()).save(any());
//    }
//}
