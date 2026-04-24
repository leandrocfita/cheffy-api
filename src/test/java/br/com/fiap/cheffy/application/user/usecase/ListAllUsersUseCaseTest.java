//package br.com.fiap.cheffy.application.user.usecase;
//
//import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
//import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
//import br.com.fiap.cheffy.domain.common.PageRequest;
//import br.com.fiap.cheffy.domain.common.PageResult;
//import br.com.fiap.cheffy.domain.profile.ProfileType;
//import br.com.fiap.cheffy.domain.profile.entity.Profile;
//import br.com.fiap.cheffy.domain.user.entity.Address;
//import br.com.fiap.cheffy.domain.user.entity.User;
//import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ListAllUsersUseCaseTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserQueryMapper mapper;
//
//    private ListAllUsersUseCase useCase;
//
//    @BeforeEach
//    void setUp() {
//        useCase = new ListAllUsersUseCase(userRepository, mapper);
//    }
//
//    @Test
//    void shouldListUsersSuccessfully() {
//        PageRequest pageRequest = PageRequest.of(0, 10, "name", PageRequest.SortDirection.ASC);
//
//        User firstUser = createUser("João Silva", "joao.silva@email.com");
//        User secondUser = createUser("Maria Souza", "maria.souza@email.com");
//        List<User> users = Arrays.asList(firstUser, secondUser);
//
//        PageResult<User> userPage = PageResult.of(users, 0, 10, 2);
//
//        UserQueryPort mappedFirstUser = mock(UserQueryPort.class);
//        UserQueryPort mappedSecondUser = mock(UserQueryPort.class);
//
//        when(userRepository.findAll(pageRequest)).thenReturn(userPage);
//        when(mapper.toQuery(firstUser)).thenReturn(mappedFirstUser);
//        when(mapper.toQuery(secondUser)).thenReturn(mappedSecondUser);
//
//
//        PageResult<UserQueryPort> usersPageResult = useCase.execute(pageRequest);
//
//        assertNotNull(usersPageResult);
//        assertEquals(2, usersPageResult.totalElements());
//        assertEquals(2, usersPageResult.numberOfElements());
//        verify(userRepository, times(1)).findAll(pageRequest);
//    }
//
//    @Test
//    void shouldReturnEmptyPageWhenNoUsers() {
//        PageRequest pageRequest = PageRequest.of(0, 10, "name", PageRequest.SortDirection.ASC);
//        PageResult<User> emptyPage = PageResult.of(Collections.emptyList(), 0, 10, 0);
//        when(userRepository.findAll(pageRequest)).thenReturn(emptyPage);
//
//
//        PageResult<UserQueryPort> usersPageResult = useCase.execute(pageRequest);
//
//        assertNotNull(usersPageResult);
//        assertEquals(0, usersPageResult.totalElements());
//        assertEquals(0, usersPageResult.numberOfElements());
//        assertTrue(usersPageResult.empty());
//        assertTrue(usersPageResult.first());
//        assertTrue(usersPageResult.last());
//
//        verify(userRepository, times(1)).findAll(pageRequest);
//        verify(mapper, never()).toQuery(any());
//    }
//
//    @Test
//    void shouldHandlePagination() {
//        PageRequest pageRequest = PageRequest.of(1, 2, "name", PageRequest.SortDirection.ASC);
//
//        User firstUser = createUser("Carlos Souza", "carlos@test.com");
//        User secondUser = createUser("Diana Costa", "diana@test.com");
//        List<User> users = Arrays.asList(firstUser, secondUser);
//
//        PageResult<User> userPage = PageResult.of(users, 1, 2, 5);
//
//        UserQueryPort queryPort3 = mock(UserQueryPort.class);
//        UserQueryPort queryPort4 = mock(UserQueryPort.class);
//
//        when(userRepository.findAll(pageRequest)).thenReturn(userPage);
//        when(mapper.toQuery(firstUser)).thenReturn(queryPort3);
//        when(mapper.toQuery(secondUser)).thenReturn(queryPort4);
//
//        PageResult<UserQueryPort> result = useCase.execute(pageRequest);
//
//        assertNotNull(result);
//        assertEquals(5, result.totalElements());
//        assertEquals(2, result.numberOfElements());
//        assertEquals(3, result.totalPages());
//        assertEquals(1, result.page());
//        assertFalse(result.first());
//        assertFalse(result.last());
//        assertFalse(result.empty());
//
//        verify(userRepository, times(1)).findAll(pageRequest);
//
//    }
//
//    private User createUser(String name, String email) {
//        Profile profile = Profile.create(1L, ProfileType.CLIENT.getType());
//        User user = User.create(
//                name,
//                email,
//                name.toLowerCase().replace(" ", "."),
//                "Pass@1234567890",
//                profile
//        );
//
//        Address address = Address.create(
//                "Avenida Paulista",
//                1000,
//                "São Paulo",
//                "01311000",
//                "Bela Vista",
//                "SP",
//                "Apartamento 101",
//                true
//        );
//
//        user.addAddress(address);
//
//        return user;
//    }
//}