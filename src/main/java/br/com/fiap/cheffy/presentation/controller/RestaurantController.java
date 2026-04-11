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
        log.info("RestaurantController.createRestaurant - START - Create restaurante- user [{}]", userId);

        var createdId = restaurantInput.execute(mapper.toCommand(restaurantCreateDTO), userId);

        log.info("RestaurantController.createRestaurant - END - Restaurant created with id [{}]", createdId);

        MDC.clear();
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateRestaurant(@PathVariable @Valid final UUID id,
                                                     @RequestParam @Valid final UUID userId) {
        log.info("RestaurantController.deactivateRestaurant - START - Deactivate restaurant - id: [{}], userId: [{}]", id, userId);
        deactivateRestaurantInput.execute(id, userId);
        log.info("RestaurantController.deactivateRestaurant - END - Restaurant deactivated - id: [{}]", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateRestaurant(@PathVariable @Valid final UUID id,
                                                     @RequestParam @Valid final UUID userId) {
        log.info("RestaurantController.reactivateRestaurant - START - Reactivate restaurant - id: [{}], userId: [{}]", id, userId);
        reactivateRestaurantInput.execute(id, userId);
        log.info("RestaurantController.reactivateRestaurant - END - Restaurant reactivated - id: [{}]", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateRestaurant(
            @PathVariable final UUID id,
            @RequestParam final UUID userId,
            @RequestBody @Valid final RestaurantUpdateDTO restaurantUpdateDTO
    ) {
        log.info("RestaurantController.updateRestaurant - START - Update restaurant - id: [{}], userId: [{}]", id, userId);
        updateRestaurantInput.execute(id, userId, mapper.toUpdateCommand(restaurantUpdateDTO));
        log.info("RestaurantController.updateRestaurant - END - Restaurant updated - id: [{}]", id);
        return ResponseEntity.noContent().build();
    }


    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantQueryPort> findRestaurantById(@PathVariable UUID id) {

        var user = findRestaurantByIdInput.execute(id);

        return ResponseEntity.ok(user);
    }


}
