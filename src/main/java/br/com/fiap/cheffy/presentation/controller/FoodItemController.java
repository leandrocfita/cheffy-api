package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.input.*;
import br.com.fiap.cheffy.presentation.config.swagger.docs.FoodItemControllerDocs;
import br.com.fiap.cheffy.presentation.dto.FoodItemAvailabilityDTO;
import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
import br.com.fiap.cheffy.presentation.dto.FoodItemUpdateDto;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/restaurants/{restaurantId}/food-items", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class FoodItemController implements FoodItemControllerDocs {

    private final CreateFoodItemInput createFoodItemInput;
    private final DeactivateFoodItemInput deactivateFoodItemInput;
    private final ReactivateFoodItemInput reactivateFoodItemInput;
    private final UpdateFoodItemAvailabilityInput updateFoodItemAvailabilityInput;
    private final UpdateFoodItemInput updateFoodItemInput;
    private final FindFoodItemByIdInput findFoodItemByIdInput;
    private final ListFoodItemsByRestaurantInput listFoodItemsByRestaurantInput;
    private final FoodItemWebMapper foodItemWebMapper;

    public FoodItemController(CreateFoodItemInput createFoodItemInput,
                              DeactivateFoodItemInput deactivateFoodItemInput,
                              ReactivateFoodItemInput reactivateFoodItemInput,
                              UpdateFoodItemAvailabilityInput updateFoodItemAvailabilityInput,
                              ListFoodItemsByRestaurantInput listFoodItemsByRestaurantInput,
                              FindFoodItemByIdInput findFoodItemByIdInput,
                              UpdateFoodItemInput updateFoodItemInput,
                              FoodItemWebMapper foodItemWebMapper) {
        this.createFoodItemInput = createFoodItemInput;
        this.findFoodItemByIdInput = findFoodItemByIdInput;
        this.listFoodItemsByRestaurantInput = listFoodItemsByRestaurantInput;
        this.deactivateFoodItemInput = deactivateFoodItemInput;
        this.reactivateFoodItemInput = reactivateFoodItemInput;
        this.updateFoodItemAvailabilityInput = updateFoodItemAvailabilityInput;
        this.foodItemWebMapper = foodItemWebMapper;
        this.updateFoodItemInput = updateFoodItemInput;
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResult<FoodItemQueryPort>> listFoodItemsByRestaurant(
            @PathVariable UUID restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "false") boolean includeInactive) {
        log.info("HTTP request received to list food items for restaurant [restaurantId={}, page={}, size={}, includeInactive={}]", restaurantId, page, size, includeInactive);
        PageRequest.SortDirection sortDirection = direction == Sort.Direction.DESC
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;
        PageResult<FoodItemQueryPort> result = listFoodItemsByRestaurantInput.execute(restaurantId, PageRequest.of(page, size, sortBy, sortDirection), includeInactive);
        log.info("Food items found successfully [restaurantId={}, page={}, size={}, sortBy={}, direction={}, includeInactive={}]", restaurantId, page, size, sortBy, direction, includeInactive);
        return ResponseEntity.ok(result);
    }

    @Override
    @PostMapping()
    @Transactional
    public ResponseEntity<FoodItemQueryPort> createFoodItem(
            @RequestBody @Valid FoodItemDTO foodItemDTO,
            @PathVariable @Valid UUID restaurantId) {
        log.info("Creating food item [foodName={}, restaurantId={}]", foodItemDTO.name(), restaurantId);
        FoodItemCommandPort foodItemCommandPort = foodItemWebMapper.foodItemDtoToFoodItemCommandPort(foodItemDTO, restaurantId);
        FoodItem createdFoodItem = createFoodItemInput.execute(foodItemCommandPort);
        FoodItemQueryPort responseObject = foodItemWebMapper.foodItemToFoodItemQueryPort(createdFoodItem);
        log.info("Food item created successfully [foodItemId={}, foodName={}, restaurantId={}]", createdFoodItem.getId(), createdFoodItem.getName(), restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    @Override
    @Transactional
    @PatchMapping("/{foodItemId}")
    public ResponseEntity<Void> updateFoodItem(
            @RequestBody @Valid FoodItemUpdateDto foodItemUpdateDTO,
            @PathVariable @Valid UUID restaurantId,
            @RequestAttribute("userId") UUID userId,
            @PathVariable @Valid UUID foodItemId
    ) {
        log.info("Updating food item [foodItemId={}, restaurantId={}, userId={}]", foodItemId, restaurantId, userId);
        FoodItemCommandPort foodItemCommandPort = foodItemWebMapper.foodItemDtoToFoodItemCommandPort(foodItemUpdateDTO, restaurantId);
        updateFoodItemInput.update(foodItemId, restaurantId, userId, foodItemCommandPort);
        log.info("Food item updated successfully [foodItemId={}, restaurantId={}, userId={}]", foodItemId, restaurantId, userId);
        return ResponseEntity.noContent().build();
    }


    @Override
    @Transactional(readOnly = true)
    @GetMapping("/{foodItemId}")
    public ResponseEntity<FoodItemQueryPort> findFoodItemById(@PathVariable UUID restaurantId, @PathVariable UUID foodItemId) {
        log.info("Finding food item [foodItemId={}, restaurantId={}]", foodItemId, restaurantId);
        FoodItemQueryPort foodItemQueryPort = findFoodItemByIdInput.execute(restaurantId, foodItemId);
        log.info("Food item found by id [foodItemId={}]", foodItemId);
        return ResponseEntity.ok(foodItemQueryPort);
    }

    @Override
    @PatchMapping("/{foodItemId}/deactivate")
    public ResponseEntity<Void> deactivateFoodItem(
            @PathVariable UUID restaurantId,
            @PathVariable UUID foodItemId) {
        log.info("HTTP request received to deactivate food item by restaurant [foodItemId={}, restaurantId={}]", foodItemId, restaurantId);
        deactivateFoodItemInput.execute(restaurantId, foodItemId);
        log.info("Food item deactivated successfully [foodItemId={}, restaurantId={}]", foodItemId, restaurantId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{foodItemId}/reactivate")
    public ResponseEntity<Void> reactivateFoodItem(
            @PathVariable UUID restaurantId,
            @PathVariable UUID foodItemId) {
        log.info("HTTP request received to reactivate food item by restaurant [foodItemId={}, restaurantId={}]", foodItemId, restaurantId);
        reactivateFoodItemInput.execute(restaurantId, foodItemId);
        log.info("Food item reactivated successfully [foodItemId={}, restaurantId={}]", foodItemId, restaurantId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{foodItemId}/availability")
    public ResponseEntity<Void> updateFoodItemAvailability(
            @PathVariable UUID restaurantId,
            @PathVariable UUID foodItemId,
            @RequestBody @Valid FoodItemAvailabilityDTO dto) {
        log.info("HTTP request received to update food item availability [foodItemId={}, restaurantId={}, available={}]", foodItemId, restaurantId, dto.available());
        updateFoodItemAvailabilityInput.execute(restaurantId, foodItemId, foodItemWebMapper.toAvailabilityCommand(dto));
        log.info("Food item availability updated successfully [foodItemId={}, restaurantId={}, available={}]", foodItemId, restaurantId, dto.available());
        return ResponseEntity.noContent().build();
    }
}
