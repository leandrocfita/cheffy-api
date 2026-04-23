package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.user.port.input.*;
import br.com.fiap.cheffy.application.user.dto.UserQueryPort;
import br.com.fiap.cheffy.infrastructure.security.model.CurrentUser;
import br.com.fiap.cheffy.infrastructure.security.resolver.CurrentUserMapper;
import br.com.fiap.cheffy.presentation.config.swagger.docs.UserControllerDocs;
import br.com.fiap.cheffy.presentation.dto.AddressCreateDTO;
import br.com.fiap.cheffy.presentation.dto.AddressPatchDTO;
import br.com.fiap.cheffy.presentation.dto.UserCreateDTO;
import br.com.fiap.cheffy.presentation.dto.UserUpdateDTO;
import br.com.fiap.cheffy.presentation.mapper.UserWebMapper;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController implements UserControllerDocs {

    private final CreateUserInput createUserInput;
    private final DeactivateUserInput deactivateUserInput;
    private final ReactivateUserInput reactivateUserInput;
//    private final UpdateUserPasswordInput updateUserPasswordInput;
    private final UpdateUserInput updateUserInput;
    private final AddAddressInput addAddressInput;
    private final UpdateAddressInput updateAddressInput;
    private final RemoveAddressInput removeAddress;
    private final ListAllUsersInput listAllUsersInput;
    private final FindUserByIdInput findUserByIdInput;
    private final FindUserByNameInput findUserByNameInput;

    private final UserWebMapper mapper;



    public UserController(
            UserWebMapper mapper,
            CreateUserInput createUserInput,
            DeactivateUserInput deactivateUserInput,
            ReactivateUserInput reactivateUserInput,
//            UpdateUserPasswordInput updateUserPasswordInput,
            UpdateUserInput updateUserInput,
            AddAddressInput addAddressInput,
            UpdateAddressInput updateAddressInput,
            RemoveAddressInput removeAddress,
            ListAllUsersInput listAllUsersInput,
            FindUserByIdInput findUserByIdInput,
            FindUserByNameInput findUserByNameInput)


    {
//        this.updateUserPasswordInput = updateUserPasswordInput;
        this.createUserInput = createUserInput;
        this.deactivateUserInput = deactivateUserInput;
        this.reactivateUserInput = reactivateUserInput;
        this.updateUserInput = updateUserInput;
        this.mapper = mapper;
        this.updateAddressInput = updateAddressInput;
        this.addAddressInput = addAddressInput;
        this.removeAddress = removeAddress;
        this.listAllUsersInput = listAllUsersInput;
        this.findUserByIdInput = findUserByIdInput;
        this.findUserByNameInput = findUserByNameInput;
    }

    @Override
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid final UserCreateDTO userCreateDTO) {
        log.info("UserController.createTbUser - START - Create user");
        var createdId = createUserInput.execute(mapper.toCommand(userCreateDTO));
        log.info("UserController.createTbUser - END - User created with id [{}]", createdId);
        MDC.clear();
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable final UUID id,
                                           @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
        log.info("UserController.updateUser - START - Update user");
        updateUserInput.execute(id, mapper.toCommand(userUpdateDTO));
        log.info("UserController.updateUser - END - User updated [{}]", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable final UUID id) {
        log.info("UserController.deactivateUser - START - Deactivate user");
        deactivateUserInput.execute(id);
        log.info("UserController.deactivateUser - END - User deactivated [{}]", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateUser(@PathVariable final UUID id) {
        log.info("UserController.reactivateUser - START - Reactivate user");
        reactivateUserInput.execute(id);
        log.info("UserController.reactivateUser - END - User reactivated [{}]", id);
        return ResponseEntity.noContent().build();
    }

    //ADDRESSES
    @Override
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<Long> addAddress(
            @PathVariable UUID userId,
            @RequestBody @Valid AddressCreateDTO dto) {

        log.info("UserController.addAddress - START - User [{}]", userId);

        addAddressInput.execute(mapper.toCommand(dto), userId);

        log.info("UserController.addAddress - END");
        MDC.clear();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @PatchMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<Void> updateAddress(
            @PathVariable UUID userId,
            @PathVariable Long addressId,
            @RequestBody @Valid AddressPatchDTO dto) {

        log.info("UserController.updateAddress - START - User [{}] Address [{}]", userId, addressId);

        updateAddressInput.execute(userId, addressId, mapper.toCommand(dto));

        log.info("UserController.updateAddress - END");
        MDC.clear();

        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{userId}/addresses/{addressId}")
    public ResponseEntity<Void> removeAddress(
            @PathVariable UUID userId,
            @PathVariable Long addressId) {

        log.info("UserController.removeAddress - START - User [{}] Address [{}]", userId, addressId);

        removeAddress.execute(userId, addressId);

        log.info("UserController.removeAddress - END");
        MDC.clear();

        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResult<UserQueryPort>> listAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        log.info("UserController.listAllUsers - START - Listing users [page={}, size={}, sortBy={}, direction={}]",
                page, size, sortBy, direction);

        PageRequest.SortDirection sortDirection = direction == Sort.Direction.DESC
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;

        PageRequest pageRequest  = PageRequest.of(page, size, sortBy, sortDirection);


        PageResult<UserQueryPort> users = listAllUsersInput.execute(pageRequest);

        log.info("UserController.listAllUsers - END - Found [{}] users in page [{}]",
                users.numberOfElements(), page);

        return ResponseEntity.ok(users);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserQueryPort> findUserById(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt jwt,
            CurrentUserMapper currentUserMapper) {
        log.info("UserController.findUserById - START - Finding user [{}]", id);

        CurrentUser cuser = currentUserMapper.from(jwt);

        //TODO: Remover, apenas para debug e exemplo
        log.info("##### CurrentUser: id={}, login={}", cuser.id(), cuser.login());


        var user = findUserByIdInput.execute(id);

        log.info("UserController.findUserById - END - User found [{}]", id);
        MDC.clear();

        return ResponseEntity.ok(user);
    }

    @Override
    @Hidden
    @GetMapping(params = "name")
    public ResponseEntity<PageResult<UserQueryPort>> searchUsersByName(
            @RequestParam @NotBlank String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        log.info("UserController.searchUsersByName - START - Searching users [name={}, page={}, size={}, sortBy={}, direction={}]",
                name, page, size, sortBy, direction);

        PageRequest.SortDirection sortDirection = direction == Sort.Direction.DESC
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;

        PageRequest pageRequest = PageRequest.of(page, size, sortBy, sortDirection);

        PageResult<UserQueryPort> users = findUserByNameInput.execute(name, pageRequest);

        log.info("UserController.searchUsersByName - END - Found [{}] users with name [{}]",
                users.numberOfElements(), name);

        return ResponseEntity.ok(users);
    }

}
