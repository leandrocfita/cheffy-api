package br.com.fiap.cheffy.application.fooditem.usecase;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemDoesNotExist;
import br.com.fiap.cheffy.domain.fooditem.port.input.UpdateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantNotFoundException;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantOperationNotAllowedException;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.shared.constants.FlowConstants;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

import java.util.UUID;
import java.util.logging.Logger;

public class UpdateFoodItemUseCase implements UpdateFoodItemInput {

    private final FoodItemRepository foodItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final Logger logger = Logger.getLogger(UpdateFoodItemUseCase.class.getName());

    public UpdateFoodItemUseCase(FoodItemRepository foodItemRepository, RestaurantRepository restaurantRepository) {
        this.foodItemRepository = foodItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public void update(UUID foodItemId, UUID restaurantId , UUID userid , FoodItemCommandPort patchFoodItem) {


       logger.info(String.format("Iniciando o fluxo de update do food-item no domínio | Flow: %s", FlowConstants.TRIAGE_UPDATE_USE_CASE_FOOD_ITEM_FLOW.getName()));

       FoodItem originalFoodItem = validateFoodItemExistence(foodItemId);

       logger.info(String.format("Existência do food item confirmada no banco de dados. foodItemId:%s | Flow: %s", originalFoodItem.getId() ,FlowConstants.TRIAGE_UPDATE_USE_CASE_FOOD_ITEM_FLOW.getName()));

       Restaurant restaurant = validateRestaurantExistence(restaurantId, originalFoodItem);

       logger.info(String.format("Existência do restaurante e posse do food item confirmadas. RestaurantId: %s | FoodItemId: %s | Flow: %s", restaurantId, foodItemId, FlowConstants.TRIAGE_UPDATE_USE_CASE_FOOD_ITEM_FLOW.getName()));

       Boolean isOwner = validateOwnership(restaurant, userid);

       if (!isOwner){
           String errorMessage = String.format("O usuário de id: %s não é dono do restaurante selecionado. RestaurantId: %s | Flow: %s",userid, restaurantId, FlowConstants.TRIAGE_UPDATE_USE_CASE_FOOD_ITEM_FLOW.getName() );

           logger.severe(errorMessage);
           throw new RestaurantOperationNotAllowedException(ExceptionsKeys.RESTAURANT_USER_DOES_NOT_HAVE_OWNERSHIP_OR_IS_INACTIVE);
       }


       FoodItem patchDomainFoodItem = FoodItem.reconstitute(
               originalFoodItem.getId(),
               patchFoodItem.name(),
               patchFoodItem.description(),
               patchFoodItem.price(),
               patchFoodItem.photoKey(),
               patchFoodItem.deliveryAvailable(),
               patchFoodItem.available(),
               patchFoodItem.active()
       );

       logger.info(String.format("O valor do id do patchFoodItem é: %s", patchDomainFoodItem.getId()));

       originalFoodItem.patch(patchDomainFoodItem);

       this.foodItemRepository.save(originalFoodItem);

       logger.info(String.format("Atualização do food-item realizada com sucesso. FoodItemId: %s | Flow: %s", originalFoodItem.getId(), FlowConstants.TRIAGE_UPDATE_USE_CASE_FOOD_ITEM_FLOW.getName()));

    }

    private Boolean validateOwnership(Restaurant restaurant, UUID userId){
        return restaurant.isOwnedByUser(userId);
    }

    private FoodItem validateFoodItemExistence(UUID foodItemId){

        FoodItem originalFoodItem = foodItemRepository.findById(foodItemId).orElse(null);

        if (originalFoodItem == null) {
            String errorMessage = String.format("Erro ao atualizar o food item: Não foi encontrado o food item na base de dados. foodItemId: %s | Flow: %s",
                    foodItemId, FlowConstants.TRIAGE_UPDATE_USE_CASE_FOOD_ITEM_FLOW.getName());

            logger.severe(errorMessage);

            throw new FoodItemDoesNotExist(ExceptionsKeys.FOOD_ITEM_DOES_NOT_EXIST, foodItemId.toString());
        }
        return  originalFoodItem;
    }

    private Restaurant validateRestaurantExistence(UUID restaurantId, FoodItem foodItem){

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        if (restaurant == null) {
            String errorMessage = String.format("Erro ao atualizar o food item: Não foi encontrado o restaurant do food item na base de dados. RestaurantId: %s | Flow: %s",
                    restaurantId, FlowConstants.TRIAGE_UPDATE_USE_CASE_FOOD_ITEM_FLOW.getName());

            logger.severe(errorMessage);

            throw new RestaurantNotFoundException(ExceptionsKeys.RESTAURANT_DOES_NOT_EXIST, restaurantId);
        }

        if (!foodItemRepository.existsInRestaurantById(restaurantId, foodItem.getId())){

            String errorMessage= String.format("O food-item não pertence ao restaurante selecionado. RestaurantId: %s | FoodItemId: %s | Flow: %s",
                    restaurantId, foodItem.getId(), FlowConstants.TRIAGE_UPDATE_USE_CASE_FOOD_ITEM_FLOW.getName());

            logger.severe(errorMessage);

            throw new FoodItemDoesNotExist(ExceptionsKeys.FOOD_ITEM_DOES_NOT_EXIST_IN_RESTAURANT, foodItem.getId().toString());
        }

        return restaurant;
    }
}
