package br.com.fiap.cheffy.infrastructure.bean_config;

import br.com.fiap.cheffy.application.address.mapper.AddressQueryMapper;
import br.com.fiap.cheffy.application.fooditem.mapper.FoodItemQueryMapper;
import br.com.fiap.cheffy.application.profile.mapper.ProfileQueryMapper;
import br.com.fiap.cheffy.application.restaurant.mapper.ResturantQueryMapper;
import br.com.fiap.cheffy.application.user.mapper.UserQueryMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationMapperConfigTest {

    @Test
    void createMappers() {
        ApplicationMapperConfig config = new ApplicationMapperConfig();

        ProfileQueryMapper profileQueryMapper = config.profileQueryMapper();
        FoodItemQueryMapper foodItemQueryMapper = config.foodItemQueryMapper();
        AddressQueryMapper addressQueryMapper = config.addressQueryMapper();
        ResturantQueryMapper resturantQueryMapper = config.resturantQueryMapper(addressQueryMapper, foodItemQueryMapper);
        UserQueryMapper userQueryMapper = config.userQueryMapper(addressQueryMapper);

        assertThat(profileQueryMapper).isNotNull();
        assertThat(foodItemQueryMapper).isNotNull();
        assertThat(resturantQueryMapper).isNotNull();
        assertThat(userQueryMapper).isNotNull();
        assertThat(addressQueryMapper).isNotNull();

    }
}
