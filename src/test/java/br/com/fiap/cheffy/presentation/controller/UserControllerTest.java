//package br.com.fiap.cheffy.presentation.controller;
//
//import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;
//import br.com.fiap.cheffy.application.user.dto.UserCommandPort;
//import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
//import br.com.fiap.cheffy.domain.common.PageResult;
//import br.com.fiap.cheffy.domain.user.port.input.*;
//import br.com.fiap.cheffy.presentation.dto.*;
//import br.com.fiap.cheffy.presentation.mapper.UserWebMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserControllerTest {
//
//    @Mock private CreateUserInput createUserInput;
//    @Mock private DeactivateUserInput deactivateUserInput;
//    @Mock private ReactivateUserInput reactivateUserInput;
//    @Mock private UpdateUserPasswordInput updateUserPasswordInput;
//    @Mock private UpdateUserInput updateUserInput;
//    @Mock private AddAddressInput addAddressInput;
//    @Mock private UpdateAddressInput updateAddressInput;
//    @Mock private RemoveAddressInput removeAddress;
//    @Mock private ListAllUsersInput listAllUsersInput;
//    @Mock private FindUserByIdInput findUserByIdInput;
//    @Mock private UserWebMapper mapper;
//
//    @InjectMocks
//    private UserController userController;
//
//    @Test
//    void createUserReturnsCreated() {
//        AddressCreateDTO address = new AddressCreateDTO("St", 1, "City", "12345678", "Hood", "ST", null, true);
//        UserCreateDTO dto = new UserCreateDTO("Name", "email@test.com", "login", "Pass123!", address);
//        String userId = UUID.randomUUID().toString();
//        when(mapper.toCommand(dto)).thenReturn(new UserCommandPort("Name", "email@test.com", "login", "Pass123!", null));
//        when(createUserInput.execute(any())).thenReturn(userId);
//
//        ResponseEntity<String> response = userController.createUser(dto);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(response.getBody()).isEqualTo(userId);
//    }
//
//    @Test
//    void updateUserPasswordReturnsOk() {
//        UUID id = UUID.randomUUID();
//        UserUpdatePasswordDTO dto = new UserUpdatePasswordDTO("NewPass@123");
//        when(mapper.toCommand(dto)).thenReturn(new UserCommandPort(null, null, null, "NewPass@123", null));
//
//        ResponseEntity<UUID> response = userController.updateUserPassword(id, dto);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo(id);
//        verify(updateUserPasswordInput).execute(any(), eq(id));
//    }
//
//    @Test
//    void updateUserReturnsNoContent() {
//        UUID id = UUID.randomUUID();
//        UserUpdateDTO dto = new UserUpdateDTO("Name", "email@test.com", "login");
//        when(mapper.toCommand(dto)).thenReturn(new UserCommandPort("Name", "email@test.com", "login", null, null));
//
//        ResponseEntity<Void> response = userController.updateUser(id, dto);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        verify(updateUserInput).execute(eq(id), any());
//    }
//
//    @Test
//    void deactivateUserReturnsNoContent() {
//        UUID id = UUID.randomUUID();
//
//        ResponseEntity<Void> response = userController.deactivateUser(id);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        verify(deactivateUserInput).execute(id);
//    }
//
//    @Test
//    void addAddressReturnsCreated() {
//        UUID userId = UUID.randomUUID();
//        AddressCreateDTO dto = new AddressCreateDTO("St", 1, "City", "12345678", "Hood", "ST", null, true);
//        when(mapper.toCommand(dto)).thenReturn(new AddressCommandPort("St", 1, "City", "12345678", "Hood", "ST", null, true));
//
//        ResponseEntity<Long> response = userController.addAddress(userId, dto);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        verify(addAddressInput).execute(any(), eq(userId));
//    }
//
//    @Test
//    void updateAddressReturnsNoContent() {
//        UUID userId = UUID.randomUUID();
//        Long addressId = 1L;
//        AddressPatchDTO dto = new AddressPatchDTO("St", 1, "City", "12345678", "Hood", "ST", null, true);
//        when(mapper.toCommand(dto)).thenReturn(new AddressCommandPort("St", 1, "City", "12345678", "Hood", "ST", null, true));
//
//        ResponseEntity<Void> response = userController.updateAddress(userId, addressId, dto);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        verify(updateAddressInput).execute(eq(userId), eq(addressId), any());
//    }
//
//    @Test
//    void removeAddressReturnsNoContent() {
//        UUID userId = UUID.randomUUID();
//        Long addressId = 1L;
//
//        ResponseEntity<Void> response = userController.removeAddress(userId, addressId);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        verify(removeAddress).execute(userId, addressId);
//    }
//
//    @Test
//    void listAllUsersReturnsOk() {
//        UserQueryPort queryPort = new UserQueryPort(UUID.randomUUID().toString(),"Name", "email@test.com", "login", true, null, null);
//        PageResult<UserQueryPort> pageResult = PageResult.of(List.of(queryPort), 0, 10, 1);
//        when(listAllUsersInput.execute(any())).thenReturn(pageResult);
//
//        ResponseEntity<?> response = userController.listAllUsers(0, 10, "name", Sort.Direction.ASC);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        verify(listAllUsersInput, times(1)).execute(any());
//    }
//
//    @Test
//    void findUserByIdReturnsOk() {
//        UUID id = UUID.randomUUID();
//        UserQueryPort queryPort = new UserQueryPort(id.toString(),"Name", "email@test.com", "login", true, null, null);
//        when(findUserByIdInput.execute(id)).thenReturn(queryPort);
//
//        ResponseEntity<?> response = userController.findUserById(id);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(response.getBody()).isEqualTo(queryPort);
//    }
//
//    @Test
//    void reactivateUserReturnsNoContent() {
//        UUID id = UUID.randomUUID();
//
//        ResponseEntity<Void> response = userController.reactivateUser(id);
//
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        verify(reactivateUserInput).execute(id);
//    }
//}
