//package br.com.fiap.cheffy.application.user.usecase;
//
//import br.com.fiap.cheffy.application.user.service.UserServiceHelper;
//import br.com.fiap.cheffy.domain.user.entity.Address;
//import br.com.fiap.cheffy.domain.user.entity.User;
//import br.com.fiap.cheffy.domain.user.exception.AddressNotFoundException;
//import br.com.fiap.cheffy.domain.user.port.output.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Set;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class RemoveAddressUseCaseTest {
//
//    @Mock
//    private UserServiceHelper userServiceHelper;
//
//    @Mock
//    private UserRepository userRepository;
//
//    private RemoveAddressUseCase removeAddressUseCase;
//
//    @BeforeEach
//    void setUp() {
//        removeAddressUseCase = new RemoveAddressUseCase(userServiceHelper, userRepository);
//    }
//
//    @Test
//    void executeRemovesAddressAndPersistsUser() {
//        UUID userId = UUID.randomUUID();
//        User user = new User(userId, "Jane Doe", "jane@example.com", "jane", "Password1!", true);
//        Address primary = new Address(10L, "Main St", 100, "City", "00000", "Downtown", "SP", null, true);
//        Address secondary = new Address(20L, "Second St", 200, "City", "11111", "Uptown", "SP", null, false);
//
//        user.addAddress(primary);
//        user.addAddress(secondary);
//
//        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);
//
//        removeAddressUseCase.execute(userId, 10L);
//
//        Set<Address> remaining = user.getAddresses();
//        assertEquals(1, remaining.size());
//        assertTrue(remaining.contains(secondary));
//        assertTrue(secondary.isMain());
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    void executeThrowsWhenAddressIsMissing() {
//        UUID userId = UUID.randomUUID();
//        User user = new User(userId, "Jane Doe", "jane@example.com", "jane", "Password1!", true);
//        Address primary = new Address(10L, "Main St", 100, "City", "00000", "Downtown", "SP", null, true);
//
//        user.addAddress(primary);
//
//        when(userServiceHelper.getUserOrFail(userId)).thenReturn(user);
//
//        assertThrows(AddressNotFoundException.class, () -> removeAddressUseCase.execute(userId, 99L));
//
//        verify(userRepository, never()).save(user);
//    }
//}
