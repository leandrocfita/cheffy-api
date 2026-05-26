package br.com.fiap.cheffy.application.user.service;

import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import br.com.fiap.cheffy.utils.UserTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceHelperTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceHelper userServiceHelper; // This will be a real instance with mocks injected

    @Nested
    @DisplayName("Tests for getUserById method")
    class GetUserByIdTests {

        @Test
        @DisplayName("Should return user when a valid id is provided")
        void shouldReturnUser_WhenValidIdIsProvided() {

            var userId = UUID.randomUUID();
            User expectedUser = UserTestUtils.createClientUserDomainEntity();
            when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

            User actualUser = userServiceHelper.getUserOrFail(userId);

            assertThat(actualUser.getId()).isEqualTo(expectedUser.getId());
            verify(userRepository).findById(userId);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when an invalid id is provided")
        void shouldThrowUserNotFoundException_WhenInvalidIdIsProvided() {
            // Arrange
            var userId = UUID.randomUUID();
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> userServiceHelper.getUserOrFail(userId))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository).findById(userId);
        }
    }
}