package br.com.fiap.cheffy.integration.fooditem;

import br.com.fiap.cheffy.application.fooditem.dto.FoodItemQueryPort;
import br.com.fiap.cheffy.domain.profile.ProfileType;
import br.com.fiap.cheffy.infrastructure.persistence.fooditem.repository.FoodItemJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.profile.repository.ProfileJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.restaurant.repository.RestaurantJpaRepository;
import br.com.fiap.cheffy.infrastructure.persistence.user.repository.UserJpaRepository;
import br.com.fiap.cheffy.integration.helper.IntegrationTestUserHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FoodItemDeleteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @Autowired
    private ProfileJpaRepository profileJpaRepository;

    @Autowired
    private FoodItemJpaRepository foodItemJpaRepository;

    @BeforeEach
    void setUp() {
        foodItemJpaRepository.deleteAll();
        restaurantJpaRepository.deleteAll();
        userJpaRepository.deleteAll();

        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.CLIENT);
        IntegrationTestUserHelper.ensureProfile(profileJpaRepository, ProfileType.OWNER);
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldDeactivateFoodItemSuccessfully() throws Exception {
        // 1. Create a User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Maria Chef",
                        "maria.chef.delete@cheffy.com",
                        "maria.chef.delete",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create a Restaurant
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante Delete",
                "Italiana",
                "11.222.333/0001-81",
                "08:00",
                "22:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua dos Restaurantes",
                        200,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 1"
                )
        );

        String restaurantId = mockMvc.perform(post("/api/v1/restaurants/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 3. Create a Food Item
        var foodItemRequest = new FoodItemCreateRequest(
                "Macarronada",
                "Massa fresca",
                new BigDecimal("45.50"),
                "photo-macarronada",
                true,
                true
        );

        String responseBody = mockMvc.perform(post("/api/v1/restaurants/{restaurantId}/food-items", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(foodItemRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        FoodItemQueryPort queryPort = objectMapper.readValue(responseBody, FoodItemQueryPort.class);

        // 4. Deactivate Food Item
        mockMvc.perform(patch("/api/v1/restaurants/{restaurantId}/food-items/{foodItemId}/deactivate", restaurantId, queryPort.id()))
                .andExpect(status().isNoContent());

        // 5. Verify in DB
        var deactivatedItem = foodItemJpaRepository.findById(queryPort.id());
        assertThat(deactivatedItem).isPresent();
        assertThat(deactivatedItem.get().getActive()).isFalse();
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenDeactivatingNonExistentFoodItem() throws Exception {
        // 1. Create a User
        String userId = IntegrationTestUserHelper.createUserAndReturnId(
                mockMvc,
                objectMapper,
                IntegrationTestUserHelper.buildUserCreateRequest(
                        "Joao Chef",
                        "joao.chef.delete@cheffy.com",
                        "joao.chef.delete",
                        "SenhaSegura@2026",
                        true
                )
        );

        // 2. Create a Restaurant
        var restaurantRequest = new RestaurantCreateRequest(
                "Restaurante Not Found",
                "Italiana",
                "11.222.333/0001-81",
                "08:00",
                "22:00",
                "America/Sao_Paulo",
                false,
                new RestaurantAddressCreateRequest(
                        "Rua dos Restaurantes",
                        200,
                        "São Paulo",
                        "01001000",
                        "Centro",
                        "SP",
                        "Sala 1"
                )
        );

        String restaurantId = mockMvc.perform(post("/api/v1/restaurants/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 3. Try to deactivate a random food item UUID
        mockMvc.perform(patch("/api/v1/restaurants/{restaurantId}/food-items/{foodItemId}/deactivate", restaurantId, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "integration-user")
    void shouldReturnNotFoundWhenDeactivatingFoodItemInNonExistentRestaurant() throws Exception {
        mockMvc.perform(patch("/api/v1/restaurants/{restaurantId}/food-items/{foodItemId}/deactivate", UUID.randomUUID(), UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    private record RestaurantCreateRequest(
            String name,
            String culinary,
            String cnpj,
            String openingTime,
            String closingTime,
            String zoneId,
            boolean open24hours,
            RestaurantAddressCreateRequest address
    ) {}

    private record RestaurantAddressCreateRequest(
            String streetName,
            Integer number,
            String city,
            String postalCode,
            String neighborhood,
            String stateProvince,
            String addressLine
    ) {}

    private record FoodItemCreateRequest(
            String name,
            String description,
            BigDecimal price,
            String photoKey,
            boolean deliveryAvailable,
            boolean available
    ) {}
}
