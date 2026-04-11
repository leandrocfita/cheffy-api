package br.com.fiap.cheffy.application.user.usecase;

import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.domain.profile.entity.Profile;
import br.com.fiap.cheffy.domain.user.entity.Address;
import br.com.fiap.cheffy.domain.user.entity.User;
import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserByNameUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserQueryMapper mapper;

    private FindUserByNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindUserByNameUseCase(userRepository, mapper);
    }

    @Test
    void shouldFindUsersByNameSuccessfully() {
        String name = "João";
        PageRequest pageRequest = PageRequest.of(0, 10);

        User firstUser = createUser("João Silva", "joao@email.com");
        User secondUser = createUser("João Pedro", "joaopedro@email.com");

        PageResult<User> userPage = PageResult.of(List.of(firstUser, secondUser), 0, 10, 2);

        UserQueryPort mappedFirst = mock(UserQueryPort.class);
        UserQueryPort mappedSecond = mock(UserQueryPort.class);

        when(userRepository.findByName(name, pageRequest)).thenReturn(userPage);
        when(mapper.toQuery(firstUser)).thenReturn(mappedFirst);
        when(mapper.toQuery(secondUser)).thenReturn(mappedSecond);

        PageResult<UserQueryPort> users = useCase.execute(name, pageRequest);

        assertNotNull(users);
        assertEquals(2, users.totalElements());
        assertEquals(2, users.numberOfElements());
        assertEquals(mappedFirst, users.content().get(0));
        assertEquals(mappedSecond, users.content().get(1));
        assertTrue(users.first());
        assertTrue(users.last());
        assertFalse(users.empty());

        verify(userRepository, times(1)).findByName(name, pageRequest);
        verify(mapper, times(1)).toQuery(firstUser);
    }

    @Test
    void shouldReturnEmptyPageWhenNoUsersFound() {
        String name = "Luan";
        PageRequest pageRequest = PageRequest.of(0, 10);

        PageResult<User> emptyPage = PageResult.of(Collections.emptyList(), 0, 10, 0);

        when(userRepository.findByName(name, pageRequest)).thenReturn(emptyPage);

        PageResult<UserQueryPort> users = useCase.execute(name, pageRequest);

        assertNotNull(users);
        assertEquals(0, users.totalElements());
        assertEquals(0, users.numberOfElements());
        assertTrue(users.empty());
        assertTrue(users.content().isEmpty());

        verify(userRepository, times(1)).findByName(name, pageRequest);
        verify(mapper, never()).toQuery(any());
    }

    @Test
    void shouldHandlePaginationCorrectly() {
        String name = "Carlos";
        PageRequest pageRequest = PageRequest.of(1, 5);

        User user = createUser("Carlos Oliveira", "carlos@email.com");

        PageResult<User> userPage = PageResult.of(List.of(user), 1, 5, 6
        );

        UserQueryPort mapped = mock(UserQueryPort.class);

        when(userRepository.findByName(name, pageRequest)).thenReturn(userPage);
        when(mapper.toQuery(user)).thenReturn(mapped);

        PageResult<UserQueryPort> users = useCase.execute(name, pageRequest);

        assertNotNull(users);
        assertEquals(6, users.totalElements());
        assertEquals(2, users.totalPages());
        assertEquals(1, users.page());
    }

    private User createUser(String name, String email) {
        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());

        User user = User.create(
                name,
                email,
                name.toLowerCase().replace(" ", "."),
                "Pass@123456",
                profile
        );

        Address address = Address.create(
                "Rua Teste",
                100,
                "São Paulo",
                "01000000",
                "Centro",
                "SP",
                "Apto 10",
                true
        );

        user.addAddress(address);

        return user;
    }
}