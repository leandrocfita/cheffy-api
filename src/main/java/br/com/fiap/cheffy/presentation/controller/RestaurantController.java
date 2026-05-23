package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
import br.com.fiap.cheffy.domain.restaurant.port.input.*;
import br.com.fiap.cheffy.infrastructure.security.model.CurrentUser;
import br.com.fiap.cheffy.infrastructure.security.resolver.CurrentUserMapper;
import br.com.fiap.cheffy.presentation.config.swagger.docs.RestaurantControllerDocs;
import br.com.fiap.cheffy.presentation.dto.RestaurantCreateDTO;
import br.com.fiap.cheffy.presentation.dto.RestaurantUpdateDTO;
import br.com.fiap.cheffy.presentation.mapper.RestaurantWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController implements RestaurantControllerDocs {

    private final RegisterRestaurantInput restaurantInput;
    private final DeactivateRestaurantInput deactivateRestaurantInput;
    private final ReactivateRestaurantInput reactivateRestaurantInput;
    private final UpdateRestaurantInput updateRestaurantInput;
    private final FindRestaurantByIdInput findRestaurantByIdInput;
    private final RestaurantWebMapper mapper;
    private final CurrentUserMapper currentUserMapper;

    public RestaurantController(
            RegisterRestaurantInput restaurantInput,
            DeactivateRestaurantInput deactivateRestaurantInput,
            ReactivateRestaurantInput reactivateRestaurantInput,
            UpdateRestaurantInput updateRestaurantInput,
            FindRestaurantByIdInput findRestaurantByIdInput,
            RestaurantWebMapper mapper, CurrentUserMapper currentUserMapper
    ) {
        this.restaurantInput = restaurantInput;
        this.deactivateRestaurantInput = deactivateRestaurantInput;
        this.reactivateRestaurantInput = reactivateRestaurantInput;
        this.updateRestaurantInput = updateRestaurantInput;
        this.findRestaurantByIdInput = findRestaurantByIdInput;
        this.mapper = mapper;
        this.currentUserMapper = currentUserMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<String> registerRestaurant(
            @RequestBody @Valid final RestaurantCreateDTO restaurantCreateDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        CurrentUser currentUser = currentUserMapper.from(jwt);
        log.info("HTTP request received to create a restaurant");
        var restaurantId = restaurantInput.execute(mapper.toCommand(restaurantCreateDTO), currentUser.id());
        log.info("Restaurant created successfully [restaurantId={}]", restaurantId);
        MDC.clear();
        return new ResponseEntity<>(restaurantId, HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateRestaurant(@PathVariable @Valid final UUID id,
                                                     @AuthenticationPrincipal Jwt jwt) {
        CurrentUser currentUser = currentUserMapper.from(jwt);
        log.info("HTTP request received to deactivate restaurant [restaurantId={}, userId={}]", id, currentUser.id());
        deactivateRestaurantInput.execute(id, currentUser.id());
        log.info("Restaurant deactivated successfully [restaurantId={}, userId={}]", id, currentUser.id());
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateRestaurant(@PathVariable @Valid final UUID id,
                                                     @AuthenticationPrincipal Jwt jwt) {
        CurrentUser currentUser = currentUserMapper.from(jwt);
        log.info("HTTP request received to reactivate restaurant [restaurantId={}, userId={}]", id, currentUser.id());
        reactivateRestaurantInput.execute(id, currentUser.id());
        log.info("Restaurant successfully reactivated [restaurantId={}, userId={}]", id, currentUser.id());
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(
            @PathVariable final UUID id,
            @RequestBody @Valid final RestaurantUpdateDTO restaurantUpdateDTO,
            @AuthenticationPrincipal Jwt jwt
    ) {
        CurrentUser currentUser = currentUserMapper.from(jwt);
        log.info("HTTP request received to update restaurant [restaurantId={}, userId={}]", id, currentUser.id());
        updateRestaurantInput.execute(id, currentUser.id(), mapper.toUpdateCommand(restaurantUpdateDTO));
        log.info("Restaurant updated successfully [restaurantId={}, userId={}]", id, currentUser.id());
        return ResponseEntity.noContent().build();
    }


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantQueryPort> findRestaurantById(@PathVariable UUID id) {
        log.info("HTTP request received to search the restaurant by id [restaurantId={}]", id);
        var restaurant = findRestaurantByIdInput.execute(id);
        log.info("Restaurant found successfully, [restaurantId={}]", id);
        return ResponseEntity.ok(restaurant);
    }


}
