package br.com.fiap.cheffy.presentation.controller;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.common.PageRequest;
import br.com.fiap.cheffy.domain.common.PageResult;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.port.input.CreateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.UpdateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.FindFoodItemByIdInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.ListFoodItemsByRestaurantInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.DeactivateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.ReactivateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.input.UpdateFoodItemAvailabilityInput;
import br.com.fiap.cheffy.presentation.config.swagger.docs.FoodItemControllerDocs;
import br.com.fiap.cheffy.presentation.dto.FoodItemAvailabilityDTO;
import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
import br.com.fiap.cheffy.presentation.dto.FoodItemUpdateDto;
import br.com.fiap.cheffy.presentation.mapper.FoodItemWebMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

        log.info("FoodItemController.listFoodItemsByRestaurant - START - restaurantId=[{}], page={}, size={}, includeInactive={}", restaurantId, page, size, includeInactive);

        PageRequest.SortDirection sortDirection = direction == Sort.Direction.DESC
                ? PageRequest.SortDirection.DESC
                : PageRequest.SortDirection.ASC;

        PageResult<FoodItemQueryPort> result = listFoodItemsByRestaurantInput.execute(restaurantId, PageRequest.of(page, size, sortBy, sortDirection), includeInactive);

        log.info("FoodItemController.listFoodItemsByRestaurant - END - Found [{}] items", result.numberOfElements());

        return ResponseEntity.ok(result);
    }

    @Override
    @PostMapping()
    @Transactional
    public ResponseEntity<FoodItemQueryPort> postFoodItem(
            @RequestBody @Valid FoodItemDTO foodItemDTO,
            @PathVariable @Valid UUID restaurantId){

        FoodItemCommandPort foodItemCommandPort = foodItemWebMapper.foodItemDtoToFoodItemCommandPort(foodItemDTO, restaurantId);

        FoodItem createdFoodItem = createFoodItemInput.execute(foodItemCommandPort);

        FoodItemQueryPort responseObject = foodItemWebMapper.foodItemToFoodItemQueryPort(createdFoodItem);

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
    ){
        FoodItemCommandPort foodItemCommandPort = foodItemWebMapper.foodItemDtoToFoodItemCommandPort(foodItemUpdateDTO, restaurantId);

        updateFoodItemInput.update(foodItemId, restaurantId, userId, foodItemCommandPort);

        return ResponseEntity.noContent().build();
    }


    @Override
    @Transactional(readOnly = true)
    @GetMapping("/{foodItemId}")
    public ResponseEntity<FoodItemQueryPort> getFoodItemById(@PathVariable UUID restaurantId, @PathVariable UUID foodItemId) {


        log.info("FoodItemController.getFoodItemById - START - Finding food item [{}] for restaurant [{}]", foodItemId, restaurantId);

        FoodItemQueryPort foodItemQueryPort = findFoodItemByIdInput.execute(restaurantId, foodItemId);

        log.info("FoodItemController.getFoodItemById - END - Food item found [{}]", foodItemId);
        return ResponseEntity.ok(foodItemQueryPort);
    }

    @Override
    @PatchMapping("/{foodItemId}/deactivate")
    public ResponseEntity<Void> deactivateFoodItem(
            @PathVariable UUID restaurantId,
            @PathVariable UUID foodItemId) {
        log.info("FoodItemController.deactivateFoodItem - START - restaurantId: [{}], foodItemId: [{}]", restaurantId, foodItemId);
        deactivateFoodItemInput.execute(restaurantId, foodItemId);
        log.info("FoodItemController.deactivateFoodItem - END - foodItemId: [{}]", foodItemId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{foodItemId}/reactivate")
    public ResponseEntity<Void> reactivateFoodItem(
            @PathVariable UUID restaurantId,
            @PathVariable UUID foodItemId) {
        log.info("FoodItemController.reactivateFoodItem - START - restaurantId: [{}], foodItemId: [{}]", restaurantId, foodItemId);
        reactivateFoodItemInput.execute(restaurantId, foodItemId);
        log.info("FoodItemController.reactivateFoodItem - END - foodItemId: [{}]", foodItemId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{foodItemId}/availability")
    public ResponseEntity<Void> updateFoodItemAvailability(
            @PathVariable UUID restaurantId,
            @PathVariable UUID foodItemId,
            @RequestBody @Valid FoodItemAvailabilityDTO dto) {
        log.info("FoodItemController.updateFoodItemAvailability - START - restaurantId: [{}], foodItemId: [{}]", restaurantId, foodItemId);
        updateFoodItemAvailabilityInput.execute(restaurantId, foodItemId, foodItemWebMapper.toAvailabilityCommand(dto));
        log.info("FoodItemController.updateFoodItemAvailability - END - foodItemId: [{}]", foodItemId);
        return ResponseEntity.noContent().build();
    }
}
