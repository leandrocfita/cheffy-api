package br.com.fiap.cheffy.application.restaurant.usecase;

import br.com.fiap.cheffy.application.restaurant.dto.UpdateRestaurantCommandPort;
import br.com.fiap.cheffy.application.restaurant.service.RestaurantServiceHelper;
import br.com.fiap.cheffy.domain.restaurant.entity.Restaurant;
import br.com.fiap.cheffy.domain.restaurant.port.input.UpdateRestaurantInput;
import br.com.fiap.cheffy.domain.restaurant.valueobject.WorkingHours;

import java.time.ZoneId;
import java.util.UUID;


public class UpdateRestaurantUseCase implements UpdateRestaurantInput {
    private final RestaurantServiceHelper restaurantServiceHelper;

    public UpdateRestaurantUseCase(RestaurantServiceHelper restaurantServiceHelper) {
        this.restaurantServiceHelper = restaurantServiceHelper;
    }

    @Override
    public void execute(UUID restaurantId, UUID userId, UpdateRestaurantCommandPort command) {
        Restaurant restaurant = restaurantServiceHelper.getRestaurantOrFailValidatingOwnership(restaurantId, userId);
        ZoneId zoneId = restaurantServiceHelper.extractZoneId(command.zoneId());
        WorkingHours workingHours = resolveWorkingHours(command, restaurant);
        restaurant.patch(
                command.name(),
                command.culinary(),
                zoneId,
                workingHours
        );
        restaurantServiceHelper.saveRestaurant(restaurant);
    }


    private WorkingHours resolveWorkingHours(UpdateRestaurantCommandPort command, Restaurant restaurant) {
        if (command.open24hours() == null && command.openingTime() == null && command.closingTime() == null) {
            return null;
        }
        boolean is24h = command.open24hours() != null ? command.open24hours() : restaurant.isOpen24hours();
        if (is24h) {
            return WorkingHours.open24Hours();
        }
        var openingTime = command.openingTime() != null ? command.openingTime() : restaurant.getOpeningTime();
        var closingTime = command.closingTime() != null ? command.closingTime() : restaurant.getClosingTime();
        return WorkingHours.of(openingTime, closingTime);
    }
}