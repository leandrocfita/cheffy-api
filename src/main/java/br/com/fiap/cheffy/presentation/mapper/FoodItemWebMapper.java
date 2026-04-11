package br.com.fiap.cheffy.presentation.mapper;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemAvailabilityCommandPort;
import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.presentation.interfaces.FoodItemRequest;
import br.com.fiap.cheffy.presentation.dto.FoodItemAvailabilityDTO;
import br.com.fiap.cheffy.presentation.dto.FoodItemDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FoodItemWebMapper {

    public FoodItemQueryPort foodItemToFoodItemQueryPort(FoodItem foodItem){
        return new FoodItemQueryPort(
                foodItem.getId(),
                foodItem.getName(),
                foodItem.getDescription(),
                foodItem.getPrice().value(),
                foodItem.getPhotoKey(),
                foodItem.getRestaurant().getId(),
                foodItem.isDeliveryAvailable(),
                foodItem.isAvailable(),
                foodItem.isActive()
        );
    }

    public <T extends FoodItemRequest> FoodItemCommandPort foodItemDtoToFoodItemCommandPort(T foodItemDTO, UUID restaurantId){

        return new FoodItemCommandPort(
                foodItemDTO.name(),
                foodItemDTO.description(),
                foodItemDTO.price(),
                foodItemDTO.photoKey(),
                restaurantId,
                foodItemDTO.deliveryAvailable(),
                foodItemDTO.available(),
                foodItemDTO.active()
        );
    }

    public FoodItemAvailabilityCommandPort toAvailabilityCommand(FoodItemAvailabilityDTO dto) {
        return new FoodItemAvailabilityCommandPort(dto.available(), dto.deliveryAvailable());
    }
}
