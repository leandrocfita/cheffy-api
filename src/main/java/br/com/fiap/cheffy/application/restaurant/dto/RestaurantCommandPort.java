package br.com.fiap.cheffy.application.restaurant.dto;

import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;

import java.time.LocalTime;

public record RestaurantCommandPort(
        String name,
        String culinary,
        String cnpj,
        LocalTime openingTime,
        LocalTime closingTime,
        String zoneId,
        boolean open24hours,
        AddressCommandPort address
) {
}
