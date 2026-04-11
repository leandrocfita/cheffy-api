package br.com.fiap.cheffy.application.user.service;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.exception.UserNotFoundException;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceHelperTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserQueryMapper mapper;

    @InjectMocks
    private UserServiceHelper userServiceHelper;

    @Test
    void getUserOrFailReturnsUser() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Name", "email@test.com", "login", "pass", true);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userServiceHelper.getUserOrFail(id);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void getUserOrFailThrowsIfNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userServiceHelper.getUserOrFail(id));
    }

    @Test
    void saveUserDelegatesToRepository() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", "pass", true);
        when(userRepository.save(user)).thenReturn(user);

        User result = userServiceHelper.saveUser(user);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void userToQueryPortMapsUser() {
        User user = new User(UUID.randomUUID(), "Name", "email@test.com", "login", "pass", true);
        UserQueryPort queryPort = new UserQueryPort(UUID.randomUUID().toString(),"Name", "email@test.com", "login", true, null, null);
        when(mapper.toQuery(user)).thenReturn(queryPort);

        UserQueryPort result = userServiceHelper.userToQueryPort(user);

        assertThat(result).isEqualTo(queryPort);
    }
}
