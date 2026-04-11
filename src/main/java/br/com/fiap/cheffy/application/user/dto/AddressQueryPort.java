package br.com.fiap.cheffy.application.user.dto;

public record AddressQueryPort(
        Long id,
        String streetName,
        Integer number,
        String city,
        String postalCode,
        String neighborhood,
        String stateProvince,
        String addressLine,
        Boolean main
) {
}
