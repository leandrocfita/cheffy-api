package br.com.fiap.cheffy.application.restaurant.dto;

import java.time.LocalTime;

public record UpdateRestaurantCommandPort(
        String name,
        String culinary,
        LocalTime openingTime,
        LocalTime closingTime,
        String zoneId,
        Boolean open24hours
) {
}