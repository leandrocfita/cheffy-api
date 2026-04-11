package br.com.fiap.cheffy.application.user.dto;

public record AddressCommandPort(

        String streetName,
        Integer number,
        String city,
        String postalCode,
        String neighborhood,
        String stateProvince,
        String addressLine,
        Boolean main
){
}
