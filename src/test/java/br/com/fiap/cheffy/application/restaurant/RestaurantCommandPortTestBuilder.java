package br.com.fiap.cheffy.application.restaurant;

import br.com.fiap.cheffy.application.restaurant.dto.RestaurantCommandPort;
import br.com.fiap.cheffy.application.user.dto.AddressCommandPort;

import java.time.LocalTime;

public class RestaurantCommandPortTestBuilder {

    private String name = "Restaurante Legal";
    private String category = "Italiana";
    private String cnpj = "27865757000102";
    private LocalTime opening = LocalTime.parse("09:00");
    private LocalTime closing = LocalTime.parse("18:00");
    private String zoneId = "America/Sao_Paulo";
    private boolean active = false;
    private boolean open24hours = false;
    private AddressCommandPort address =
            new AddressCommandPort(
                    "Rua A",
                    123,
                    "Sao Paulo",
                    "01001000",
                    "Centro",
                    "SP",
                    "Loja 1",
                    null
            );

    public static RestaurantCommandPortTestBuilder aValidCommand() {
        return new RestaurantCommandPortTestBuilder();
    }

    public RestaurantCommandPortTestBuilder withZoneId(String zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    public RestaurantCommandPortTestBuilder withOpening(LocalTime opening) {
        this.opening = opening;
        return this;
    }

    public RestaurantCommandPortTestBuilder withClosing(LocalTime closing) {
        this.closing = closing;
        return this;
    }

    public RestaurantCommandPortTestBuilder withOpen24hours(boolean open24hours) {
        this.open24hours = open24hours;
        return this;
    }

    public RestaurantCommandPortTestBuilder withCnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public RestaurantCommandPort build() {
        return new RestaurantCommandPort(
                name,
                category,
                cnpj,
                opening,
                closing,
                zoneId,
                active,
                address
        );
    }
}