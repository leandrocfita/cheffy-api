package br.com.fiap.cheffy.application.fooditem.usecase;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemCommandPort;
import br.com.fiap.cheffy.domain.fooditem.entity.FoodItem;
import br.com.fiap.cheffy.domain.fooditem.exception.FoodItemAlreadyExistInRestaurant;
import br.com.fiap.cheffy.domain.fooditem.port.input.CreateFoodItemInput;
import br.com.fiap.cheffy.domain.fooditem.port.output.FoodItemRepository;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.exception.RestaurantDoesNotExistException;
import br.com.fiap.cheffy.domain.restaurant.port.output.RestaurantRepository;
import br.com.fiap.cheffy.shared.constants.FlowConstants;
import br.com.fiap.cheffy.shared.exception.keys.ExceptionsKeys;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateFoodItemUseCase implements CreateFoodItemInput {
    
    private final Logger logger = Logger.getLogger(CreateFoodItemUseCase.class.getName());
    private final FoodItemRepository foodItemRepository;
    private final RestaurantRepository restaurantRepository;
    
    public CreateFoodItemUseCase(FoodItemRepository foodItemRepository, RestaurantRepository restaurantRepository) {
        this.foodItemRepository = foodItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public FoodItem execute(FoodItemCommandPort foodItemCommandPort) {

        logger.info(String.format("Iniciando o cadastro do item: %s para o restaurante: %s | Flow: %s", foodItemCommandPort.name(), foodItemCommandPort.restaurantId(), FlowConstants.TRIAGE_CREATE_USE_CASE_FOOD_ITEM_FLOW.getName()));

        Restaurant restaurant = restaurantRepository.findById(foodItemCommandPort.restaurantId()).orElseThrow(
                () -> {
                    String errorMessage = String.format("Erro ao tentar cadastrar o item: %s no cárdapio do restaurante: %s. O restaurante não existe. | Flow: %s", foodItemCommandPort.name(), foodItemCommandPort.restaurantId(), FlowConstants.TRIAGE_CREATE_USE_CASE_FOOD_ITEM_FLOW);
                    logger.log(Level.SEVERE, errorMessage);

                    return new RestaurantDoesNotExistException(ExceptionsKeys.RESTAURANT_DOES_NOT_EXIST, foodItemCommandPort.restaurantId().toString());
                }
        );
        
        if(foodItemRepository.existsByNameIgnoreCaseAndRestaurantId(foodItemCommandPort.name(), foodItemCommandPort.restaurantId())){

            String errorMessage = String.format("Erro ao tentar cadastrar o item: %s no cárdapio do restaurante: %s. O item já existe no cardápio. Flow: %s", foodItemCommandPort.name(), foodItemCommandPort.restaurantId(), FlowConstants.TRIAGE_CREATE_USE_CASE_FOOD_ITEM_FLOW);
            logger.log(Level.SEVERE, errorMessage);

            throw new FoodItemAlreadyExistInRestaurant(ExceptionsKeys.FOOD_ITEM_ALREADY_EXIST, foodItemCommandPort.name());
        }

        logger.info(String.format("Convertendo o foodItemCommandPort para FoodItem | Flow: %s", FlowConstants.TRIAGE_CREATE_USE_CASE_FOOD_ITEM_FLOW.getName()));

        FoodItem createdFoodItem =  FoodItem.create(foodItemCommandPort.name(), foodItemCommandPort.description(), foodItemCommandPort.price(), foodItemCommandPort.photoKey(), foodItemCommandPort.deliveryAvailable(), foodItemCommandPort.available(), restaurant);

        logger.info(String.format("Iniciando o salvamento no banco de dados para o FoodItem criado pelo dominio | Flow: %s", FlowConstants.TRIAGE_CREATE_USE_CASE_FOOD_ITEM_FLOW.getName()));

        return foodItemRepository.save(createdFoodItem);
    }
}
