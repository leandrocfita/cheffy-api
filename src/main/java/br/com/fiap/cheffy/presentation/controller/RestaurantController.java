package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantQueryPort;
import br.com.fiap.cheffy.domain.restaurant.port.input.*;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultApiErrors;
import br.com.fiap.cheffy.presentation.config.doc_helper.DefaultNotFoundApiResponse;
import br.com.fiap.cheffy.presentation.config.swagger.docs.RestaurantControllerDocs;
import br.com.fiap.cheffy.presentation.dto.RestaurantCreateDTO;
import br.com.fiap.cheffy.presentation.dto.RestaurantUpdateDTO;
import br.com.fiap.cheffy.presentation.exceptionhandler.model.Problem;
import br.com.fiap.cheffy.presentation.mapper.RestaurantWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public RestaurantController(
            RegisterRestaurantInput restaurantInput,
            DeactivateRestaurantInput deactivateRestaurantInput,
            ReactivateRestaurantInput reactivateRestaurantInput,
            UpdateRestaurantInput updateRestaurantInput,
            FindRestaurantByIdInput findRestaurantByIdInput,
            RestaurantWebMapper mapper
    ) {
        this.restaurantInput = restaurantInput;
        this.deactivateRestaurantInput = deactivateRestaurantInput;
        this.reactivateRestaurantInput = reactivateRestaurantInput;
        this.updateRestaurantInput = updateRestaurantInput;
        this.findRestaurantByIdInput = findRestaurantByIdInput;
        this.mapper = mapper;
    }

    @Override
    @PostMapping("/{userId}")
    public ResponseEntity<String> registerRestaurant(
            @RequestBody @Valid final RestaurantCreateDTO restaurantCreateDTO,
            @PathVariable @Valid final UUID userId
    ) {
        log.info("HTTP request received to create a restaurant");
        var restaurantId = restaurantInput.execute(mapper.toCommand(restaurantCreateDTO), userId);
        log.info("Restaurant created successfully [restaurantId={}]", restaurantId);
        MDC.clear();
        return new ResponseEntity<>(restaurantId, HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateRestaurant(@PathVariable @Valid final UUID id,
                                                     @RequestParam @Valid final UUID userId) {
        log.info("HTTP request received to deactivate restaurant [restaurantId={}, userId={}]", id, userId);
        deactivateRestaurantInput.execute(id, userId);
        log.info("Restaurant deactivated successfully [restaurantId={}, userId={}]", id, userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateRestaurant(@PathVariable @Valid final UUID id,
                                                     @RequestParam @Valid final UUID userId) {
        log.info("HTTP request received to reactivate restaurant [restaurantId={}, userId={}]", id, userId);
        reactivateRestaurantInput.execute(id, userId);
        log.info("Restaurant successfully reactivated [restaurantId={}, userId={}]", id, userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(
            @PathVariable final UUID id,
            @RequestParam final UUID userId,
            @RequestBody @Valid final RestaurantUpdateDTO restaurantUpdateDTO
    ) {
        log.info("HTTP request received to update restaurant [restaurantId={}, userId={}]", id, userId);
        updateRestaurantInput.execute(id, userId, mapper.toUpdateCommand(restaurantUpdateDTO));
        log.info("Restaurant updated successfully [restaurantId={}, userId={}]", id, userId);
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
